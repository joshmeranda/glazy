package com.jmeranda.glazy.lib.handler

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.Repo

import com.jmeranda.glazy.lib.request.RepoPatchRequest

/**
 * Handle a PATCH request to edit a repository
 *
 * @property request The request object used by the handler.
 * @property token The personal access token of the user.
 */
class RepoPatchHandler(
        private val request: RepoPatchRequest,
        token: String?
): Handler<Repo>(token) {
    private val repositoryUrl: String = endpoints.repositoryUrl

    override fun handleRequest(): Repo {
        var body: String? = null

        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        handleCode(response.statusCode)

        return mapper.readValue(response.text)
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.request.owner)
            .replace("{repo}", this.request.currentName)
}