package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.RootEndpoints
import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.service.CacheService

/**
 * Get a all available github endpoints as of v3.
 *
 * @param mapper Used to parse api root endpoint response.
 * @return Endpoint? deserialize json response
 */
fun getRootEndpoints(rootUrl: String, mapper: ObjectMapper): RootEndpoints {
    val rootEndpoints: RootEndpoints

    try {
        val endpointsAsJson: String = get(rootUrl).text
        rootEndpoints = mapper.readValue(endpointsAsJson)
    } catch (e: Exception) {
        throw BadEndpoint()
    }

    CacheService.write(rootEndpoints)

    return rootEndpoints
}

/**
 * A http request handler.
 *
 * Send http request to github api, and deserialize the Json response.
 *
 * @property token The personal access token of the user.
 */
abstract class Handler<T>(private val token: String?) {
    /**
     * Get request header for authorizing with personal access token.
     *
     * @return A map of the header field to the personal access token.
     */
    protected fun getAuthorizationHeaders(): Map<String, String> {
        return if (this.token == null) {
            emptyMap()
        } else {
            mapOf("Authorization" to "token ${this.token}")
            }
    }

    /**
     * Handle the request.
     *
     * @return The object response from the api.
     */
    abstract fun handleRequest(): Any?

    /**
     * Get the endpoint url for the request.
     *
     * @return The url to query.
     */
    abstract fun getRequestUrl(): String

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        val endpoints: RootEndpoints = CacheService.endpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, mapper)

        /**
         * Notify user with message depending on the value of [statusCode].
         */
        fun handleCode(statusCode: Int) {
            when (statusCode) {
                404 -> println("Resource could not be found or accessed.\n" +
                        "Please ensure that you have proper permissions, and that it exists")
                403 -> println("Resource could not be found or accessed.\n" +
                        "Please ensure that you have proper permissions, and that it exists")
            }
        }
    }
}