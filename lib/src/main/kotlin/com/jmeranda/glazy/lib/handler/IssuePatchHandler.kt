package com.jmeranda.glazy.lib.handler

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssuePatchRequest

/**
 * Handle a PATCH request to edit an issue.
 *
 * @property request The request object used by the handler.
 * @property number The number of the issue to edit.
 * @property token The personal access token of the user.
 */
class IssuePatchHandler(
        private val request: IssuePatchRequest,
        private val number: Int,
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

        val response: Response = patch(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        handleCode(response.statusCode)

        return mapper.readValue(response.text)
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}", "/$number")
}