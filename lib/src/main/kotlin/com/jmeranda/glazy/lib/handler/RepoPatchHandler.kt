package com.jmeranda.glazy.lib.handler

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.Repo

import com.jmeranda.glazy.lib.request.RepoPatchRequest

/**
 * Handle a PATCH request to edit a repository
 *
 * @property repoRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class RepoPatchHandler(
        private val repoRequest: RepoPatchRequest,
        token: String?
): Handler<Repo>(token) {
    private val repositoryUrl: String = Handler.endpoints.repositoryUrl

    override fun handleRequest(): Repo {
        var body: String? = null

        try {
            body = Handler.writer().writeValueAsString(this.repoRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        return Handler.reader().readValue(response.text)
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.repoRequest.owner)
            .replace("{repo}", this.repoRequest.currentName)
}