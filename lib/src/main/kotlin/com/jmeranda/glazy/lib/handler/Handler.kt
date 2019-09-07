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
 * Return a RootEndpoints instance serialed using [mapper].
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
 * Abstract class for all handler classes used to parse a request object
 * and send queries to an api. The initial endpoint urls are stored in
 * [endpoints], and all response json is serialized using [mapper].
 * Requests are authenticated using the given [token].
 */
abstract class Handler<T>(private val token: String?) {
    /**
     * Return a map containing the header used to authenticate with the
     * instances token.
     */
    protected fun getAuthorizationHeaders(): Map<String, String> {
        return if (this.token == null) {
            emptyMap()
        } else {
            mapOf("Authorization" to "token ${this.token}")
        }
    }

    /**
     * Return the requested date received from the api.
     */
    abstract fun handleRequest(): Any?

    /**
     * Return the endpoint url for the request.
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