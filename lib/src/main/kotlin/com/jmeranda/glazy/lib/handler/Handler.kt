package com.jmeranda.glazy.lib.handler

import com.beust.klaxon.FieldRenamer
import com.beust.klaxon.Klaxon
import com.jmeranda.glazy.lib.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.RootEndpoints
import com.jmeranda.glazy.lib.exception.BadEndpoint
import khttp.get

fun getKlaxonFieldRenamer(): Klaxon {
    val fieldRenamer: FieldRenamer = object: FieldRenamer {
        override fun toJson(fieldName: String) = FieldRenamer.camelToUnderscores(fieldName)
        override fun fromJson(fieldName: String) = FieldRenamer.underscoreToCamel(fieldName)
    }

    return Klaxon().fieldRenamer(fieldRenamer)
}

/**
 * Get a all available github endpoints as of v3.
 *
 * @param klaxonParser used to parse api root endpoint response.
 * @param cache ResponseCache instance to store the api response.
 *
 * @return Endpoint? deserialize json response
 */
private fun getRootEndpoints(klaxonParser: Klaxon, cache: ResponseCache): RootEndpoints {
    val endpointsAsJson: String = get(ROOT_ENDPOINT).text
    var rootEndpoints: RootEndpoints?

    try {
        rootEndpoints = klaxonParser.parse<RootEndpoints>(endpointsAsJson)
    } catch (e: Exception) {
        rootEndpoints = null
    }

    if (rootEndpoints != null) { cache.write(rootEndpoints) }

    return rootEndpoints ?: throw BadEndpoint()
}

/**
 * A http request handler.
 *
 * Send http request to github api, and deserialize the Json response.
 */
abstract class Handler(private val token: String? = null) {
    protected fun getAuthorizationHeaders(): Map<String, String> {
        return if (this.token == null) {
            mapOf("" to "")
        } else {
            mapOf("Authorization" to "token ${this.token}")
            }
    }

    abstract fun handleRequest(): Any?

    abstract fun getRequestUrl(): String

    protected companion object {
        val fieldRenameKlaxon: Klaxon = getKlaxonFieldRenamer()

        val cache = ResponseCache()

        val endpoints: RootEndpoints = cache.endpoints()
                ?: getRootEndpoints(fieldRenameKlaxon, cache)
    }
}