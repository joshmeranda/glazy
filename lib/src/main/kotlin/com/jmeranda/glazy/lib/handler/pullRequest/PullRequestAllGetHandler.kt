package com.jmeranda.glazy.lib.handler.pullRequest

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler

import khttp.get
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.request.PullGetAllRequest

class PullRequestAllGetHandler(
        private val request: PullGetAllRequest,
        token: String?
): Handler<PullRequest>(token) {
    private val repositoryUrl: String = endpoints.repositoryUrl

    override fun handleRequest(): List<PullRequest>? {
        val headers = this.getAuthorizationHeaders()
                .toMutableMap()
        headers["Accept"] = "application/vnd.github.shadow-cat-preview+json"
        val response: Response = get(this.getRequestUrl(), headers = headers)
        var pullRequest: List<PullRequest>? = null

        if ( !handleCode(response)) return null

        try {
            pullRequest = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pullRequest
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.request.owner)
            .replace("{repo}", this.request.name)
            .plus("/pulls")
}