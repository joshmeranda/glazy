/**
 * Store some API responses to limit the amount of requests made.
 */
package com.jmeranda.glazy.lib.handler

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
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Data class used for parsing cached access-token pairs
 *
 * @param user The user login key for each token.
 * @param token The token mapped to each user.
 */
data class UserTokenPair(
        val user: String,
        val token: String
)

/**
 * Reads and Writes cached API data in json format.
 *
 * All cached data is stored in .cache/lazy in the users HOME directory.
 */
class ResponseCache {
    companion object {
        /**
         *  Read cached repository data, give the repo [name] and [user].
         *
         *  @return Repo if cached data exists, null otherwise.
         */
        fun repo(name: String, user: String): Repo? {
            val fileName = "$CACHE_DIR/$user/$name.json"
            if (!Files.exists(Paths.get(fileName))) return null
            val target = File(fileName)

            return mapper.readValue(target)
        }

        /**
         * Get the cached private access token of the [user].
         *
         * @return The access token associated with the specified user.
         */
        fun token(user: String): String? {
            if (! Files.exists(Paths.get(TOKEN_FILE))) return null

            val tokenFile = File(TOKEN_FILE)
            val tokenArray: List<UserTokenPair> = mapper.readValue(tokenFile)
            var token: String? = null

            for (pair: UserTokenPair in tokenArray) {
                if (pair.user == user) { token = pair.token }
            }

            return token
        }

        /**
         * Read cached root endpoints data.
         *
         * @return RootEndpoints if cached data exists, null otherwise
         */
        fun endpoints(): RootEndpoints? {
            if (! Files.exists(Paths.get(ENDPOINTS_FILE))) return null

            val target = File(ENDPOINTS_FILE)

            return mapper.readValue(target)
        }

        /**
         * Write repository data to .cache/glazy in HOME directory.
         *
         * @param data Repo to be cached.
         */
        fun write(data: Repo) {
            if (! Files.exists(Paths.get("$CACHE_DIR/${data.owner.login}"))) {
                File("$CACHE_DIR/${data.owner.login}").mkdir()
            }

            val fileName = "$CACHE_DIR/${data.fullName}.json"
            val dest = File(fileName)
            val repoAsJson = mapper.writeValueAsString(data)

            try {
                dest.createNewFile()
                dest.writeText(repoAsJson)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Write root endpoints dat to .cache/glazy in HOME directory.
         *
         * @param data Root endpoints to be cached.
         */
        fun write(data: RootEndpoints) {
            val dest = File(ENDPOINTS_FILE)

            try {
                dest.createNewFile()
                dest.writeText(mapper.writeValueAsString((data)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Write access [token] to .cache/glazy/access_tokens.json given a [user].
         */
        fun write(user: String, token: String) {
            val tokenFile = File(TOKEN_FILE)
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
         * Clear the cache of data ignoring access token depending on [ignoreToken].
         */
        fun clear(ignoreToken: Boolean) {
            val cacheDir = File(CACHE_DIR)

            if (! ignoreToken) {
                cacheDir.deleteRecursively()
            } else {
                for (file in cacheDir.listFiles() ?: arrayOf()) {
                    if (file == File(TOKEN_FILE)) continue
                    file.deleteRecursively()
                }
            }
        }

        /**
         * Refresh the cache from the thing in the thing that does the thing.
         */
        fun refresh(user: String, name: String, token: String?, service: RepoService = RepoService(token)) {
            /* Replace cached repo data */
            val path = "$CACHE_DIR/$user/$name"
            File(path).delete()
            write(service.getRepo(name, user))
        }

        /**
         * Refresh all data using the specified access [token].
         */
        fun refresh(token: String?) {
            val service = RepoService(token)
            val cacheDir = File(CACHE_DIR)

            /* Iterate through all cached data ignore access tokens */
            for (file in cacheDir.listFiles() ?: arrayOf()) {

                /* If file is a directory, refresh cached repository data */
                if (file.isDirectory) {
                    for (cachedRepo in file.listFiles() ?: arrayOf()) {
                        refresh(file.name, cacheDir.name, token, service)
                    }
                }

                /* Refresh endpoints data */
                if (file.name == ENDPOINTS_FILE) {
                    write(endpoints() ?: continue)
                }

            }
        }

        val CACHE_DIR = "${System.getProperty("user.home")}/.cache/glazy"

        private val TOKEN_FILE = "$CACHE_DIR/access_tokens.json"

        private val ENDPOINTS_FILE = "$CACHE_DIR/root_endpoints.json"

        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    }
}