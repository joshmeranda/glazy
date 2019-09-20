package com.jmeranda.glazy.lib.handler.repo

import com.jmeranda.glazy.lib.handler.Handler
import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoTransferRequest

/**
 * Handle a [request] to transfer ownership of a repository using the
 * specified [token].
 */
class RepoTransferHandler(
        private val request: RepoTransferRequest,
        token: String?
): Handler(token) {
    override fun handleRequest() {
        var body: String? = null
        val headers = this.getAuthorizationHeaders()
                .toMutableMap()
        headers["Accept"] = "application/vnd.github.nightshade-preview+json"

        // Deserialize the request.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = headers)

        handleCode(response)
    }
    
    override fun getRequestUrl(): String = endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/transfer")
}