package com.jmeranda.gitkot.lib.handler

import khttp.get

import com.jmeranda.gitkot.lib.Issue
import com.jmeranda.gitkot.lib.request.IssueRequest

class IssueGetHandler(private val issueRequest: IssueRequest): Handler() {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): Issue? {
        val issueAsJson = get(this.getRequestUrl()).text
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
            .replace("{/number}", this.issueRequest.number.toString())
}