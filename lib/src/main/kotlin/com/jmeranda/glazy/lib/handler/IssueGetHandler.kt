package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueGetRequest

/**
 * Handle GET request for a specific issue.
 *
 * @property request The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssueGetHandler(
        private val request: IssueGetRequest,
        token: String?
): Handler<Issue>(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): Issue? {
        val response = get(this.getRequestUrl(), headers=this.getAuthorizationHeaders())
        var issue: Issue?

        handleCode(response.statusCode)

        try {
            issue = mapper.readValue(response.text)
        } catch (e: Exception) {
            issue = null
        }

        return issue
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}", "/${this.request.number.toString()}")
}