package com.jmeranda.glazy.lib.handler

import com.fasterxml.jackson.module.kotlin.readValue
import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.request.PullPostRequest


class PullRequestPostHandler (
        private val request: PullPostRequest,
        token: String?
): Handler<PullRequest>(token) {

    override fun handleRequest(): PullRequest? {
        var body: String? = null

        // Deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Prepare and send post request to api
        val headers = this.getAuthorizationHeaders()
                .toMutableMap()
        headers["Accept"] = "application/vnd.github.shadow-cat-preview+json"
        val response: Response = post(this.getRequestUrl(),
                data  = body,
                headers = headers)

        if (! handleCode(response)) return null

        var pullRequest: PullRequest? = null

        // // Serialize the received json.
        try {
            pullRequest = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pullRequest
    }

    override fun getRequestUrl(): String = endpoints.repositoryUrl
            .replace("{owner}", this.request.owner)
            .replace("{repo}", this.request.name)
            .plus("/pulls")
}