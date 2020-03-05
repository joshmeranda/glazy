package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.objects.*
import com.jmeranda.glazy.lib.service.rootEndpoints
import com.jmeranda.glazy.lib.service.write

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import java.util.logging.*

import khttp.*
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
 * Formatter for handler logging.
 */
private class HandlerFormatter : Formatter() {
    override fun format(record: LogRecord?): String {
        val builder = StringBuilder()

        builder.append("${record?.level?.name}: ")
        builder.append(record?.message)
        builder.append("\n")

        return builder.toString()
    }
}

/**
 * Handles requests for resources.
 *
 * @param header The header for sending requests.
 * @param url The url for generating endpoint targets.
 * @param clazz The expected return type from requests.
 */
abstract class Handler(
    private val header: GlazyHeader,
    protected val url: String,
    private val request: Request,
    private val clazz: KClass<out GitObject>
) : GlazyHeader by header {
    /**
     * Serialize the api request object.
     *
     * @return The serialized object or null on error.
     */
    protected fun serializeRequest(): String? {
        return try {
            log(Level.INFO,"Creating api response.")
            mapper.writeValueAsString(this.request)
        } catch (e: JsonMappingException) {
            log(Level.WARNING, "Error mapping api request.")
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
            log(Level.INFO, "Parsing api response.")
            mapper.readValue(data, this.clazz.java)
        } catch (e: JsonMappingException) {
            log(Level.WARNING, "Error parsing api response.")
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
            log(Level.INFO, "Parsing api response.")
            when (this.clazz) {
                Issue::class -> mapper.readValue<List<Issue>>(data)
                Repo::class -> mapper.readValue<List<Repo>>(data)
                Label::class -> mapper.readValue<List<Label>>(data)
                PullRequest::class -> mapper.readValue<List<PullRequest>>(data)
                User::class -> mapper.readValue<List<User>>(data)
                else -> null
            }
        } catch (e: JsonMappingException) {
            log(Level.WARNING, "Error parsing api response.")
            null
        }
    }

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

        val endpoints: RootEndpoints = rootEndpoints()
                ?: getRootEndpoints(ROOT_ENDPOINT, mapper)

        protected val logger: Logger = Logger.getLogger(Handler::class.qualifiedName)

        var verbose = false
            set(value) {
                // sets the logger level
                logger.level = if (value) { Level.ALL } else { Level.OFF }
                if (value) {
                    logger.useParentHandlers = false
                    val handler = ConsoleHandler()
                    handler.formatter = HandlerFormatter()
                    logger.addHandler(handler)
                }

                field = value
            }

        init {
            logger.level = Level.OFF
        }

        /**
         * Provide access to logging progress to the console.
         *
         * @param msg The message to be logged.
         */
        fun log(level: Level, msg: String) {
            logger.log(level, msg)
        }

        /**
         * Handle the request response to determine is success or failure.
         *
         * @param response The [khttp.responses.Response] received.
         * @return True on success, false on failure
         */
        fun handleCode(response: Response): Boolean {
            if (response.statusCode >= 400) {
                log(Level.WARNING, response.jsonObject["message"] as String)
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
    url: String,
    request: Request,
    clazz: KClass<out GitObject>
) : Handler(header, url, request, clazz), SingleResponse, ListResponse {
    override fun handleListRequest(): List<GitObject>? {
        log(Level.INFO, "Sending request to ${this.url}")

        val response = get(this.url, headers = this.headers)
        if (! handleCode(response)) return null

        return deserializeList(response.text)
    }

    override fun handleRequest(): GitObject? {
        log(Level.INFO, "Sending request to ${this.url}")

        val response = get(this.url, headers = this.headers)
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
    url: String,
    request: Request,
    clazz: KClass<out GitObject>
) : Handler(header, url, request, clazz), SingleResponse, NoResponse {
    override fun handleNoRequest() {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = post(this.url, data = serializeRequest(), headers = this.headers)

        handleCode(response)
    }

    override fun handleRequest(): GitObject? {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = post(this.url, data = serializeRequest(), headers = this.headers)
        if (! handleCode(response)) return null

        return deserialize(response.text)
    }
}

/**
 * Handler for PUT style requests/
 *
 * @see Handler
 */
class PutHandler(
    header: GlazyHeader,
    url: String,
    request: Request,
    clazz: KClass<out GitObject>
) : Handler(header, url, request, clazz), SingleResponse, NoResponse {
    override fun handleNoRequest() {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = put(this.url, data = serializeRequest(), headers = this.headers)

        handleCode(response)
    }

    override fun handleRequest(): GitObject? {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = put(this.url, data = serializeRequest(), headers = this.headers)
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
    url: String,
    request: Request,
    clazz: KClass<out GitObject>
) : Handler(header, url, request, clazz), SingleResponse {
    override fun handleRequest(): GitObject? {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = patch(this.url, data = serializeRequest(), headers = this.headers)
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
    url: String,
    request: Request,
    clazz: KClass<out GitObject>
) : Handler(header, url, request, clazz), NoResponse {
    override fun handleNoRequest() {
        log(Level.INFO, "Sending request to ${this.url}")

        val response: Response = delete(this.url, headers = this.headers)

        handleCode(response)
    }
}
