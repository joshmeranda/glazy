package com.jmeranda.glazy.lib.handler

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssuePatchRequest

/**
 * Handle a [request] to patch a repository issue using the specified [token].
 */
class IssuePatchHandler(
        private val request: IssuePatchRequest,
        token: String?
): Handler<Issue>(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): Issue? {
        var body: String? = null

        // Deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders())

        handleCode(response.statusCode)

        var issue: Issue? = null

        // Serialize received json.
        try {
            issue = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return issue
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}", "/${request.number}")
}