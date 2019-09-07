package com.jmeranda.glazy.lib.service

import java.lang.System
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.RootEndpoints

/**
 * Static class used for cache operations.
 */
class CacheService {
    companion object {
        /**
         * Data class which associates a [user] to an access [token].
         */
        data class UserTokenPair(
                val user: String,
                val token: String
        )

        private var CACHE_PATH = "${System.getProperty("user.home")}/.cache/glazy"

        private var TOKEN_CACHE_PATH = "$CACHE_PATH/access_tokens.json"

        private var ENDPOINT_CACHE_PATH = "$CACHE_PATH/root_endpoints.json"

        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        /**
         * Determine if [cachePath] exists and create it if not, the path
         * is created as either a file or directory depending on the value
         * of [isDir].
         */
        private fun ensureCache(cachePath: String, isDir: Boolean = false) {
            // Determine if the path exists.
            if (!  Files.exists(Paths.get(cachePath))) {
                val cacheFile = File(cachePath)

                // Create the cachePat.
                if (isDir) {
                    cacheFile.mkdirs()
                } else {
                    cacheFile.parentFile.mkdirs()
                    File(cachePath).createNewFile()
                }
            }
        }

        /**
         * Change the location of the cache to [path] from the default.
         */
        fun setCacheLocation(path: String) {
            CACHE_PATH = path
            TOKEN_CACHE_PATH = "$CACHE_PATH/access_tokens.json"
            ENDPOINT_CACHE_PATH = "$CACHE_PATH/root_endpoints.json"
        }

        /**
         * Return a repo instance given the [user] who owns the repository
         * called [name].
         */
        fun repo(user: String, name: String): Repo? {
            val fileName = "$CACHE_PATH/$user/$name.json"
            if (!Files.exists(Paths.get(fileName))) return null
            val target = File(fileName)

            return mapper.readValue(target)
        }

        /**
         * Return the cached personal access token of the [user].
         */
        fun token(user: String): String? {
            // If the token cache file does not exist sdo nothing.
            if (! Files.exists(Paths.get(TOKEN_CACHE_PATH))) return null

            // Deserialize the cache file's content and search for the user.
            val tokenFile = File(TOKEN_CACHE_PATH)
            val tokenArray: List<UserTokenPair> = mapper.readValue(tokenFile)
            var token: String? = null

            for (pair: UserTokenPair in tokenArray) {
                if (pair.user == user) { token = pair.token }
            }

            return token
        }

        /**
         * Return the cached api root endpoints.
         */
        fun endpoints(): RootEndpoints? {
            if (! Files.exists(Paths.get(ENDPOINT_CACHE_PATH))) return null

            val target = File(ENDPOINT_CACHE_PATH)

            return mapper.readValue(target)
        }

        /**
         * Write [data] to the appropriate repository cache.
         */
        fun write(data: Repo) {
            val repoCachePath = "$CACHE_PATH/${data.fullName}.json"

            // Ensure the owner directory as well as the repository file.
            ensureCache(repoCachePath)

            val repoAsJson = mapper.writeValueAsString(data)

            try {
                File(repoCachePath).writeText(repoAsJson)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Write the [data] to the ENDPOINT_CACHE_PATH.
         */
        fun write(data: RootEndpoints) {
            ensureCache(ENDPOINT_CACHE_PATH)

            try {
                File(ENDPOINT_CACHE_PATH).writeText(mapper.writeValueAsString((data)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Write the [user] and [token] pair to the TOKEN_CACHE_PATH.
         */
        fun write(user: String, token: String) {
            ensureCache(TOKEN_CACHE_PATH)

            val tokenFile = File(TOKEN_CACHE_PATH)
            val tokenList: MutableList<UserTokenPair> = (mapper.readValue(tokenFile) as List<UserTokenPair>)
                    .toMutableList()
            tokenList.add(UserTokenPair(user, token))

            try {
                tokenFile.writeText(mapper.writeValueAsString(tokenList))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Clear the cache of data ignoring TOKEN_CACHE_PATH depending
         * on [ignoreToken].
         */
        fun clear(ignoreToken: Boolean) {
            if (! Files.exists(Paths.get(CACHE_PATH))) return

            val cacheDir = File(CACHE_PATH)

            if (! ignoreToken) {
                cacheDir.deleteRecursively()
            } else {
                for (file in cacheDir.listFiles() ?: arrayOf()) {
                    if (file == File(TOKEN_CACHE_PATH)) continue
                    file.deleteRecursively()
                }
            }
        }

        /**
         * Refresh the cache of the repository owned by [user] called
         * [name], using the provided [service].
         */
        fun refresh(user: String, name: String, service: RepoService) {
            if (! Files.exists(Paths.get("$CACHE_PATH/user/name")))  return

            /* Replace cached repo data */
            val path = "$CACHE_PATH/$user/$name"
            File(path).delete()
            write(service.getRepo(user, name))
        }

        /**
         * Refresh all data using the specified access [token].
         */
        fun refresh(token: String?) {
            if (! Files.exists(Paths.get(CACHE_PATH))) return

            val service = RepoService(token)
            val cacheDir = File(CACHE_PATH)

            /* Iterate through all cached data ignore access tokens */
            for (file in cacheDir.listFiles() ?: arrayOf()) {

                /* If file is a directory, refresh cached repository data */
                if (file.isDirectory) {
                    for (cachedRepo in file.listFiles() ?: arrayOf()) {
                        refresh(file.name, cacheDir.name, service)
                    }
                }

                /* Refresh endpoints data */
                if (file.name == ENDPOINT_CACHE_PATH) {
                    write(endpoints()
                            ?: continue)
                }

            }
        }
    }
}