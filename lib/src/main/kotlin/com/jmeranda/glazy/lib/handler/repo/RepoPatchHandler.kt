package com.jmeranda.glazy.lib.handler.repo

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Repo

import com.jmeranda.glazy.lib.request.RepoPatchRequest

/**
 * Handle a [request] to edit a remote repository using the specified [token].
 */
class RepoPatchHandler(
        private val request: RepoPatchRequest,
        token: String?
): Handler(token) {
    private val repositoryUrl: String = endpoints.repositoryUrl

    override fun handleRequest(): Repo? {
        var body: String? = null

        // Deserialize the request object.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        if (! handleCode(response)) return null

        var repo: Repo? = null

        // Serialize the received json.
        try {
            repo = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.request.owner)
            .replace("{repo}", this.request.currentName)
}