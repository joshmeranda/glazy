package com.jmeranda.gitkot.lib.handler

import com.jmeranda.gitkot.lib.Issue
import com.jmeranda.gitkot.lib.request.IssueAllRequest

class IssueAllGetHandler(private val issueRequest: IssueAllRequest): Handler() {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): Set<Issue>? {
        val issueAsJson = khttp.get(this.getRequestUrl()).text
        var issue: Set<Issue>?

        try {
            issue = Handler.fieldRenameKlaxon.parse(issueAsJson)
        } catch (e: Exception) {
            issue = null
        }

        return issue
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("{/number}","")
}