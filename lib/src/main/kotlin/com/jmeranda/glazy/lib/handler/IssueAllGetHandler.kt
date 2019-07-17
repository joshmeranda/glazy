package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueAllRequest

class IssueAllGetHandler(
        private val issueRequest: IssueAllRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): List<Issue>? {
        val issueAsJson =
                khttp.get(this.getRequestUrl(), headers=this.getAuthorizationHeaders()).text
        var allIssues: List<Issue>?

        try {
            allIssues = Handler.fieldRenameKlaxon.parseArray(issueAsJson)
        } catch (e: Exception) {
            allIssues = null
            e.printStackTrace()
        }

        return allIssues
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("{/number}","")
}