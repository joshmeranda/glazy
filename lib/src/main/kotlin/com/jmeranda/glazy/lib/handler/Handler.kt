package com.jmeranda.glazy.lib.handler

import khttp.get

import com.beust.klaxon.FieldRenamer
import com.beust.klaxon.Klaxon

import com.jmeranda.glazy.lib.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.RootEndpoints
import com.jmeranda.glazy.lib.exception.BadEndpoint

/**
 * Get klaxon parser for renaming camel case to snake case.
 * @return field renaming Klaxon instance.
 */
fun getKlaxonFieldRenamer(): Klaxon {
    val fieldRenamer: FieldRenamer = object: FieldRenamer {
        override fun toJson(fieldName: String) = FieldRenamer.camelToUnderscores(fieldName)
        override fun fromJson(fieldName: String) = FieldRenamer.underscoreToCamel(fieldName)
    }

    return Klaxon().fieldRenamer(fieldRenamer)
}

/**
 * Get a all available github endpoints as of v3.
 * @param klaxonParser used to parse api root endpoint response.
 * @param cache ResponseCache instance to store the api response.
 * @return Endpoint? deserialize json response
 */
fun getRootEndpoints(rootUrl: String, klaxonParser: Klaxon,
                             cache: ResponseCache
): RootEndpoints {
    var rootEndpoints: RootEndpoints?

    try {
        val endpointsAsJson: String = get(rootUrl).text
        rootEndpoints = klaxonParser.parse<RootEndpoints>(endpointsAsJson)
    } catch (e: Exception) {
        rootEndpoints = null
    }

    if (rootEndpoints != null) { cache.write(rootEndpoints) }

    return rootEndpoints ?: throw BadEndpoint()
}

/**
 * A http request handler.
 * Send http request to github api, and deserialize the Json response.
 * @property token The personal access token of the user.
 */
abstract class Handler(private val token: String?) {
    /**
     * Get request header for authorizing with personal access token.
     * @return A map of the header field to the personal access token.
     */
    protected fun getAuthorizationHeaders(): Map<String, String> {
        return if (this.token == null) {
            mapOf("" to "")
        } else {
            mapOf("Authorization" to "token ${this.token}")
            }
    }

    /**
     * Handle the request.
     * @return The object response from the api.
     */
    abstract fun handleRequest(): Any?

    /**
     * Get the endpoint url for the request.
     * @return The url to query.
     */
    abstract fun getRequestUrl(): String

    protected companion object {
        val fieldRenameKlaxon: Klaxon = getKlaxonFieldRenamer()

        val cache = ResponseCache()

        val endpoints: RootEndpoints = cache.endpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, fieldRenameKlaxon, cache)
    }
}