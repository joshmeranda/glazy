package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchIssue
import com.jmeranda.glazy.lib.handler.IssueAllGetHandler
import com.jmeranda.glazy.lib.handler.IssueGetHandler
import com.jmeranda.glazy.lib.request.IssueAllRequest
import com.jmeranda.glazy.lib.request.IssueRequest

/**
 * Client service to construct requests and return response data.
 */
class IssueService(
        private val repo: Repo,
        private val token: String?
) {
    fun getAllIssues(): List<Issue> {
        val issueAllRequest = IssueAllRequest(this.repo)
        val issueAllGetHandler = IssueAllGetHandler(issueAllRequest, this.token)

        return issueAllGetHandler.handleRequest() ?: throw Exception()
    }

    fun getIssue(number: Int): Issue {
        val issueRequest = IssueRequest(this.repo, number)
        val issueHandler = IssueGetHandler(issueRequest, this.token)

        return issueHandler.handleRequest() ?: throw NoSuchIssue(number)
    }
}