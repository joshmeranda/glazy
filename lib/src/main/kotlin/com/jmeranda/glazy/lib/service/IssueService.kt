package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.IssueAllGetHandler
import com.jmeranda.glazy.lib.handler.IssueGetHandler
import com.jmeranda.glazy.lib.request.IssueRequest
import com.jmeranda.glazy.lib.request.IssueAllRequest

/**
 * Client service to construct requests and return response data.
 */
class IssueService() : RepoService() {
    fun getAllIssues(name: String, user: String): Set<Issue>? {
        val repo: Repo = this.getRepo(name, user) ?: throw NoSuchRepo(name)
        val issueAllRequest = IssueAllRequest(repo)
        val issueAllGetHandler = IssueAllGetHandler(issueAllRequest)

        return issueAllGetHandler.handleRequest()
    }

    fun getIssue(name: String, user: String, number: Int): Issue? {
        val repo: Repo = this.getRepo(name, user) ?: throw NoSuchRepo(name)
        val issueRequest: IssueRequest = IssueRequest(repo, number)
        val issueHandler = IssueGetHandler(issueRequest)

        return issueHandler.handleRequest()
    }
}