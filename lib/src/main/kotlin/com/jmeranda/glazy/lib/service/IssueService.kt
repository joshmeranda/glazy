package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.exception.BadRequest
import com.jmeranda.glazy.lib.exception.NoSuchIssue
import com.jmeranda.glazy.lib.handler.GlazyIssueUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazySimpleIssueUrl
import com.jmeranda.glazy.lib.handler.issue.IssueAllGetHandler
import com.jmeranda.glazy.lib.handler.issue.IssueGetHandler
import com.jmeranda.glazy.lib.handler.issue.IssuePatchHandler
import com.jmeranda.glazy.lib.handler.issue.IssuePostHandler
import com.jmeranda.glazy.lib.request.IssueGetAllRequest
import com.jmeranda.glazy.lib.request.IssueGetRequest
import com.jmeranda.glazy.lib.request.IssuePatchRequest
import com.jmeranda.glazy.lib.request.IssuePostRequest

/**
 * Class to run remote issue operations for the specified [repo], using
 * [token] for authentication.
 */
class IssueService(
        private val repo: Repo,
        private val token: String?
) {
    /**
     * Return the repository issue associated with the value of [number].
     */
    fun getIssue(number: Int): Issue {
        val request = IssueGetRequest(this.repo.owner.login, this.repo.name, number)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyIssueUrl(request)

        val issueHandler = IssueGetHandler(header, url)

        return issueHandler.handleRequest() ?: throw NoSuchIssue(number)
    }

    /**
     * Return a list of every issue in the current repository.
     */
    fun getAllIssues(): List<Issue> {
        val request = IssueGetAllRequest(this.repo.owner.login, this.repo.name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleIssueUrl(request)

        val issueAllGetHandler = IssueAllGetHandler(header, url)

        return issueAllGetHandler.handleRequest() ?: throw Exception()
    }

    /**
     * Create and return a new issue in the repository given the issue's
     * [title], [body], [milestone], [labels], and [assignees].
     */
    fun createIssue(title: String, body: String?, milestone: Int?,
                    labels: List<String>?, assignees: List<String>?
    ): Issue {
        val request = IssuePostRequest(this.repo.owner.login, this.repo.name, title, body, milestone, labels, assignees)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleIssueUrl(request)

        val issueHandler = IssuePostHandler(header, url)

        return issueHandler.handleRequest() ?: throw BadRequest("Could not create issue.")
    }

    /**
     * Edit an issue's [title], [body], [state], [milestone], [labels],
     * or [assignees] given the issue's [number] and return the edited
     * issue.
     */
    fun editIssue(number: Int, title: String?, body: String?, state: String?,
                  milestone: Int?, labels: List<String>?, assignees: List<String>?
    ): Issue {
        val request = IssuePatchRequest(this.repo.owner.login, this.repo.name, number, title,
                body, state, milestone, labels, assignees)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyIssueUrl(request)
        val issueHandler = IssuePatchHandler(header, url)

        return issueHandler.handleRequest() ?: throw BadRequest()
    }
}
