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
import com.jmeranda.glazy.lib.service.cache.*
import khttp.responses.Response
import kotlin.reflect.KClass

/**
 * Retrieve root endpoints.
 *
 * @param rootUrl The topmost api endpoint url.
 * @param mapper The object mapper to use for deserialization.
 * @return An instance of [com.jmeranda.glazy.lib.objects.RootEndpoints]
 */
fun getRootEndpoints(rootUrl: String, mapper: ObjectMapper): RootEndpoints {
    val rootEndpoints: RootEndpoints

    try {
        val endpointsAsJson: String = get(rootUrl).text
        rootEndpoints = mapper.readValue(endpointsAsJson)
    } catch (e: Exception) {
        throw BadEndpoint(rootUrl)
    }

    write(rootEndpoints)

    return rootEndpoints
}

/**
 * Handles requests for resources.
 *
 * @param header The header for sending requests.
 * @param url The url for generating endpoint targets.
 * @param clazz The expected return type from for requests.
 */
abstract class Handler(
        private val header: GlazyHeader,
        protected val url: GlazyUrl,
        private val clazz: KClass<out GitObject>
) : GlazyHeader by header, GlazyUrl by url {

    /**
     * Serialize the api request object.
     *
     * @return The serialized object or null on error.
     */
    protected fun serializeRequest(): String? {
        return try {
            mapper.writeValueAsString(this.request)
        } catch (e: JsonMappingException) {
            println("Error mapping api request.")
            null
        }
    }

    /**
     * Deserialize a string into the desired type, must be a subclass of [com.jmeranda.glazy.lib.objects.GitObject].
     *
     * @param data The data to be deserialized.
     * @return The deserialized object, or null on deserialization error.
     */
    fun deserialize(data: String): GitObject? {
        return try {
            mapper.readValue(data, this.clazz.java)
        } catch (e: JsonMappingException) {
            println("Error parsing api response.")
            null
        }
    }

    /**
     * Deserialize a string into a list of the desired type, which must be a subclass of [com.jmeranda.glazy.lib.objects.GitObject].
     *
     * @param data The data to be deserialized.
     * @return The deserialized object, or null on deserialization error.
     */
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

        val endpoints: RootEndpoints = rootEndpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, mapper)

        /**
         * Handle the request response to determine is success or failure.
         *
         * @param response The [khttp.responses.Response] received.
         * @return True on success, false on failure
         */
        fun handleCode(response: Response): Boolean {
            if (response.statusCode >= 400) {
                println(response.jsonObject["message"])
            }

            return response.statusCode < 400
        }
    }
}

/**
 * Interface for handlers not expecting to receive information.
 */
interface NoResponse {
    /**
     * Handle requests expectong no response object.
     */
    fun handleNoRequest()
}

/**
 * Interface for handlers expecting to receive a  object.
 */
interface SingleResponse {
    /**
     * Handle requests expecting a response object.
     */
    fun handleRequest(): GitObject?
}

/**
 * Interface for handlers expecting to recevie a list of objects.
 */
interface ListResponse {
    /**
     * Handle requests expecting a list of response objects.
     */
    fun handleListRequest(): List<GitObject>?
}

/**
 * Handler for GET style requests.
 *
 * @see Handler
 */
class GetHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), SingleResponse, ListResponse {
    override fun handleListRequest(): List<GitObject>? {
        val response = get(this.requestUrl, headers = this.headers)
        if (! handleCode(response)) return null

        return deserializeList(response.text)
    }

    override fun handleRequest(): GitObject? {
        val response = get(this.requestUrl, headers = this.headers)
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

/**
 * Handler for POST style requests.
 *
 * @see Handler
 */
class PostHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), SingleResponse, NoResponse {
    override fun handleNoRequest() {
        val body: String? = serializeRequest()
        val response: Response = post(this.requestUrl,
            data = body,
            headers = this.headers)

        handleCode(response)
    }

    override fun handleRequest(): GitObject? {
        val response: Response = post(this.requestUrl,
                data = serializeRequest(),
                headers = this.headers)
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

/**
 * Handler for PATCH style requests.
 *
 * @see Handler
 */
class PatchHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), SingleResponse {
    override fun handleRequest(): GitObject? {
        val response: Response = patch(this.requestUrl,
                data = serializeRequest(),
                headers = this.headers)
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

/**
 * Handler for DELETE style requests.
 *
 * @see Handler
 */
class DeleteHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz), NoResponse {
    override fun handleNoRequest() {
        val response: Response = delete(this.requestUrl,
                headers = this.headers)

        ! handleCode(response)
    }
}
