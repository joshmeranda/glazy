package com.jmeranda.glazy.lib.handler

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import khttp.get
import khttp.post
import khttp.delete
import khttp.patch

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.objects.*
import com.jmeranda.glazy.lib.service.CacheService
import khttp.responses.Response
import kotlin.reflect.KClass

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
        private val header: GlazyHeader,
        protected val url: GlazyUrl,
        private val clazz: KClass<out GitObject>
) : GlazyHeader by header, GlazyUrl by url {
    protected fun serializeRequest(): String? {
        return try {
            mapper.writeValueAsString(this.request)
        } catch (e: JsonMappingException) {
            println("Error mapping api request.")
            null
        }
    }

    fun deserialize(data: String): GitObject? {
        return try {
            mapper.readValue(data, this.clazz.java)
        } catch (e: JsonMappingException) {
            println("Error parsing api response.")
            null
        }
    }

    protected fun deserializeList(data: String): List<GitObject>? {
        return try {
            when (this.clazz) {
                Issue::class -> mapper.readValue<List<Issue>>(data)
                Repo::class -> mapper.readValue<List<Repo>>(data)
                Label::class -> mapper.readValue<List<Label>>(data)
                PullRequest::class -> mapper.readValue<List<PullRequest>>(data)
                else -> null
            }
        } catch (e: JsonMappingException) {
            println("Error parsing api response.")
            null
        }
    }

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        val endpoints: RootEndpoints = CacheService.endpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, mapper)

        /**
         * Handle the http [response] and return true if error code received.
         */
        fun handleCode(response: Response): Boolean {
            return if (response.statusCode >= 400) {
                println(response.jsonObject["message"])
                false
            } else {
                true
            }
        }
    }
}

interface NoResponse {
    fun handleNoRequest()
}

interface SingleResponse {
    fun handleRequest(): GitObject?
}

interface ListResponse {
    fun handleListRequest(): List<GitObject>?
}

class GetHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), SingleResponse, ListResponse {
    override fun handleListRequest(): List<GitObject>? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        if (! handleCode(response)) return null

        return deserializeList(response.text)
    }

    override fun handleRequest(): GitObject? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

class PostPatchHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), SingleResponse {
    override fun handleRequest(): GitObject? {
        val response: Response = patch(this.requestUrl,
                data = serializeRequest(),
                headers = this.getHeaders())
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

class DeleteHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), NoResponse {
    override fun handleNoRequest() {
        val response: Response = delete(this.requestUrl,
                headers = this.getHeaders())

        ! handleCode(response)
    }
}

class TransferHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), NoResponse {
    override fun handleNoRequest() {
        val body: String? = serializeRequest()
        val response: Response = post(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        handleCode(response)
    }
}