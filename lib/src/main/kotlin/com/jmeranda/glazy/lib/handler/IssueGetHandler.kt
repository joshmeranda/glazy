package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueRequest
import khttp.get

class IssueGetHandler(
        private val issueRequest: IssueRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): Issue? {
        val issueAsJson =
                get(this.getRequestUrl(), headers=this.getAuthorizationHeaders()).text
        var issue: Issue?

        try {
            issue = Handler.fieldRenameKlaxon.parse<Issue>(issueAsJson)
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