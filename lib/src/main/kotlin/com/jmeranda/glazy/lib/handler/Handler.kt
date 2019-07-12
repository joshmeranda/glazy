package com.jmeranda.glazy.lib.handler

import com.beust.klaxon.FieldRenamer
import com.beust.klaxon.Klaxon

import khttp.get

import com.jmeranda.glazy.lib.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.RootEndpoints
import com.jmeranda.glazy.lib.exception.BadEndpoint

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
 * @return Endpoint? deserialized json response
 */
private fun getRootEndpoints(klaxon: Klaxon): RootEndpoints {
    val endpointsAsJson: String = get(ROOT_ENDPOINT).text
    var rootEndpoints: RootEndpoints?

    try {
        rootEndpoints = klaxon.parse<RootEndpoints>(endpointsAsJson)
    } catch (e: Exception) {
        rootEndpoints = null
    }

    return rootEndpoints ?: throw BadEndpoint()
}

/**
 * A http request handler.
 *
 * Send http request to github api, and deserialize the Json response.
 */
abstract class Handler {
    abstract fun handleRequest(): Any?

    abstract fun getRequestUrl(): String

    companion object {
        val fieldRenameKlaxon: Klaxon = getKlaxonFieldRenamer()

        val endpoints: RootEndpoints = getRootEndpoints(fieldRenameKlaxon)
    }
}