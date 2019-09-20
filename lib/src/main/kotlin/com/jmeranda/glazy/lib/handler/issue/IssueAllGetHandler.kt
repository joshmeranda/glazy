package com.jmeranda.glazy.lib.handler.issue

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.request.IssueGetAllRequest

/**
 * Handle a [request] for all repository issues using the specified [token].
 */
class IssueAllGetHandler(
        private val request: IssueGetAllRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): List<Issue>? {
        val response = get(this.getRequestUrl(), headers=this.getAuthorizationHeaders())
        var allIssues: List<Issue>? = null

        if (! handleCode(response)) return null

        // Serialize the received json.
        try {
            allIssues = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allIssues
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}","")
}