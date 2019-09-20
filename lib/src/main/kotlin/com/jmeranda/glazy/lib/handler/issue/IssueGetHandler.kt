package com.jmeranda.glazy.lib.handler.issue

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.request.IssueGetRequest

/**
 * Handle a [request] for a specific repository issue using the specified [token].
 */
class IssueGetHandler(
        private val request: IssueGetRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): Issue? {
        val response = get(this.getRequestUrl(), headers=this.getAuthorizationHeaders())
        var issue: Issue? = null

        if (! handleCode(response)) return null

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
            .replace("{/number}", "/${this.request.number.toString()}")
}