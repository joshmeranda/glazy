package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.objects.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.objects.RootEndpoints
import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.service.CacheService
import khttp.responses.Response

/**
 * Return a RootEndpoints instance serialized using [mapper].
 */
fun getRootEndpoints(rootUrl: String, mapper: ObjectMapper): RootEndpoints {
    val rootEndpoints: RootEndpoints

    try {
        val endpointsAsJson: String = get(rootUrl).text
        rootEndpoints = mapper.readValue(endpointsAsJson)
    } catch (e: Exception) {
        throw BadEndpoint(rootUrl)
    }

    CacheService.write(rootEndpoints)

    return rootEndpoints
}

abstract class Handler(
        protected val header: GlazyHeader,
        protected val url: GlazyUrl
) : GlazyHeader by header, GlazyUrl by url {

    abstract fun handleRequest(): Any?

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        val endpoints: RootEndpoints = CacheService.endpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, mapper)

        /**
         * Handle the http [response] and return true if error code received.
         */
        fun handleCode(response: Response): Boolean {
            if (response.statusCode >= 400) {
                println(response.jsonObject["message"])
                return false
            }

            return true
        }
    }
}