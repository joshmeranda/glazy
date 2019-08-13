package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueGetRequest

/**
 * Handle GET request for a specific.
 *
 * @property issueRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssueGetHandler(
        private val issueRequest: IssueGetRequest,
        token: String?
): Handler<Issue>(token) {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): Issue? {
        val issueAsJson =
                get(this.getRequestUrl(), headers=this.getAuthorizationHeaders()).text
        var issue: Issue?

        try {
            issue = Handler.mapper.readValue(issueAsJson)
        } catch (e: Exception) {
            issue = null
        }

        return issue
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("{/number}", "/${this.issueRequest.number.toString()}")
}