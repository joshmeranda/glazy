package com.jmeranda.gitkot.lib.handler

import com.beust.klaxon.FieldRenamer
import com.beust.klaxon.Klaxon

import khttp.get

import com.jmeranda.gitkot.lib.BASE_URL
import com.jmeranda.gitkot.lib.Endpoints
import com.jmeranda.gitkot.lib.request.Request

/* Allow for simpler Json deserialization (EG) stud_puffin <-> studPuffin */
val fieldRenamer = object: FieldRenamer {
    override fun toJson(fieldName: String) = FieldRenamer.camelToUnderscores(fieldName)
    override fun fromJson(fieldName: String) = FieldRenamer.underscoreToCamel(fieldName)
}
internal val klaxon = Klaxon().fieldRenamer(fieldRenamer)

/**
 * Get a class representing all available github endpoint as of v3.
 *
 * @return Endpoint? deserialized json response
 */
fun getEndpoints(): Endpoints? {
    val endpointsAsJson: String = get(BASE_URL).text
    return klaxon.parse<Endpoints>(endpointsAsJson)
}

/**
 * A http request handler.
 *
 * Send http request to github api, and deserialize the Json response.
 *
 * @property request Request instance describing the request to be made.
 */
interface Handler {
    companion object {
        val endpoints: Endpoints? = getEndpoints()
    }

    val request: Request

    fun handleRequest(): Any?
}