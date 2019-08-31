package com.jmeranda.glazy.lib.handler

import khttp.post
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssuePostRequest

/**
 * Handle a POST request for a new issue.
 *
 * @property request The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssuePostHandler(
        private val request: IssuePostRequest,
        token: String?
): Handler<Issue>(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): Issue? {
        var body: String? = null

        try {
            body = mapper.writeValueAsString(this.request)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        handleCode(response.statusCode)

        return mapper.readValue(response.text)
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}","")
}