package com.jmeranda.gitkot.lib.service

import com.jmeranda.gitkot.lib.Issue
import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.exception.NoSuchRepo
import com.jmeranda.gitkot.lib.handler.IssueAllGetHandler
import com.jmeranda.gitkot.lib.handler.IssueGetHandler
import com.jmeranda.gitkot.lib.request.IssueRequest
import com.jmeranda.gitkot.lib.request.IssueAllRequest

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