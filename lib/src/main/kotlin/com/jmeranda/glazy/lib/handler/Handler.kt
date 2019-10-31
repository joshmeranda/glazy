package com.jmeranda.glazy.lib.handler

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import khttp.*

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
        protected val clazz: KClass<out GitObject>
) : GlazyHeader by header, GlazyUrl by url {
    abstract fun handleRequest(): GitObject?

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

class GetHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz) {
    fun handleListRequest(): String {
        val response = get(this.requestUrl, headers = this.getHeaders())
        val data = mutableListOf<GitObject>()

        if (! handleCode(response)) return String()

        return response.text

//        try {
//            for (i in 0..response.jsonArray.length()) {
//                println(response.jsonArray.getJSONObject(i).get("repository_url"))
//                println(mapper.convertValue(response.jsonArray.getJSONObject(i), type))
//                data[i] = mapper.readValue(
//                        mapper.writeValueAsString(response.jsonArray.getJSONObject(i)),
//                        type
//                )
//            }
//        } catch (e: JsonMappingException) {
//            e.printStackTrace()
//            println("Error mapping api response")
//        }

//        try {
//            data = mapper.readValue<List<HashMap<String, String>>>(response.text)
//        } catch (e: JsonMappingException) {
//            println("Error parsing api response.")
//        }

//        return data.toList()
    }

    override fun handleRequest(): GitObject? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        var data: GitObject? = null

        if (! handleCode(response)) return null

        try {
            data = when (clazz) {
                Issue::class -> mapper.readValue(response.text, Issue::class.java)
                Repo::class -> mapper.readValue(response.text, Repo::class.java)
                Label::class -> mapper.readValue(response.text, Label::class.java)
                PullRequest::class -> mapper.readValue(response.text, PullRequest::class.java)
                else -> null
            }
        } catch (e: JsonMappingException) {
            println("Error parsing api response.")
            e.printStackTrace()
        }

        return data
    }
}

class PostPatchHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz) {
    override fun handleRequest(): GitObject? {
        var body: String? = null

        // deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: JsonMappingException) {
            println("Error mapping api request.")
        }

        val response: Response = patch(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (! handleCode(response)) return null

        var data: GitObject? = null

        // Serialize the received json.

        try {
            data = mapper.readValue(response.text)
        } catch (e: JsonMappingException) {
            println("Error mapping api response")
        }

        return data
    }
}

class DeleteHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz) {
    override fun handleRequest(): GitObject? {
        val response: Response = delete(this.requestUrl,
                headers = this.getHeaders())

        if (! handleCode(response)) return null

        return null
    }
}

class TransferHandler(
        header: GlazyHeader,
        url: GlazyUrl,
        clazz: KClass<out GitObject>
) : Handler(header, url, clazz) {
    override fun handleRequest(): GitObject? {
        var body: String? = null
        val headers = this.getHeaders()

        // Deserialize the request.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: JsonMappingException) {
            println("Error mapping request.")
        }

        val response: Response = post(this.requestUrl,
                data = body,
                headers = headers)

        handleCode(response)

        return null
    }
}