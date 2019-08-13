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
    init {
        if (! Files.exists(Paths.get(CACHE_DIR))) {
            File(CACHE_DIR).mkdir()
        }
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
        val repoAsJson = ResponseCache.mapper.writeValueAsString(data)

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
        val fileName = "$CACHE_DIR/root_endpoints.json"
        val dest = File(fileName)
        val endpointsAsJson = ResponseCache.mapper.writeValueAsString(data)

        try {
            dest.createNewFile()
            dest.writeText(endpointsAsJson)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Write access [token] to .cache/glazy/access_tokens.json given a [user].
     */
    fun write(user: String, token: String) {
        /* TODO Not yet implemented */
    }

    /**
     *  Read cached repository data, give the repo [name] and [user].
     *
     *  @return Repo if cached data exists, null otherwise.
     */
    fun repo(name: String, user: String): Repo? {
        val fileName = "$CACHE_DIR/$user/$name.json"
        if (!Files.exists(Paths.get(fileName))) { return null }
        val target = File(fileName)

        return ResponseCache.mapper.readValue(target)
    }

    /**
     * Get the cached private access token of the [user].
     *
     * @return The access token associated with the specified user.
     */
    fun token(user: String): String? {
        val tokenFile = File("$CACHE_DIR/access_tokens.json")
        val tokenArray: List<UserTokenPair> = ResponseCache.mapper.readValue(tokenFile)
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
        val fileName = "$CACHE_DIR/root_endpoints.json"
        if (! Files.exists(Paths.get(fileName))) { return null }
        val target = File(fileName)

        return ResponseCache.mapper.readValue(target)
    }

    companion object {
        val CACHE_DIR = "${System.getProperty("user.home")}/.cache/glazy"

        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    }
}