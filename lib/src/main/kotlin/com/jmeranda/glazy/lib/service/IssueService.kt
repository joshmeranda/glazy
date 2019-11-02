package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.request.IssueGetAllRequest
import com.jmeranda.glazy.lib.request.IssueGetRequest
import com.jmeranda.glazy.lib.request.IssuePatchRequest
import com.jmeranda.glazy.lib.request.IssuePostRequest

/**
 * Class to run remote issue operations for the specified [repo], using
 * [token] for authentication.
 */
class IssueService(
        private val user: String,
        private val name: String,
        private val token: String?
) {
    /**
     * Return the repository issue associated with the value of [number].
     */
    fun getIssue(number: Int): Issue? {
        val request = IssueGetRequest(this.user, this.name, number)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyIssueUrl(request)
        val handler = GetHandler(header, url, Issue::class)

        return handler.handleRequest() as Issue?
    }

    /**
     * Return a list of every issue in the current repository.
     */
    fun getAllIssues(): List<Issue>? {
        val request = IssueGetAllRequest(this.user, this.name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleIssueUrl(request)
        val handler = GetHandler(header, url, Issue::class)

        return handler.handleListRequest() as List<Issue>?
    }

    /**
     * Create and return a new issue in the repository given the issue's
     * [title], [body], [milestone], [labels], and [assignees].
     */
    fun createIssue(
            title: String,
            body: String? = null,
            milestone: Int? = null,
            labels: List<String>? = null,
            assignees: List<String>? = null
    ): Issue? {
        val request = IssuePostRequest(this.user, this.name, title, body, milestone, labels, assignees)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleIssueUrl(request)
        val issueHandler = PostHandler(header, url, Issue::class)

        return issueHandler.handleRequest() as Issue?
    }

    /**
     * Edit an issue's [title], [body], [state], [milestone], [labels],
     * or [assignees] given the issue's [number] and return the edited
     * issue.
     */
    fun editIssue(
            number: Int,
            title: String? = null,
            body: String? = null,
            state: String? = null,
            milestone: Int? = null,
            labels: List<String>? = null,
            assignees: List<String>? = null
    ): Issue? {
        val request = IssuePatchRequest(this.user, this.name, number, title,
                body, state, milestone, labels, assignees)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyIssueUrl(request)
        val issueHandler = PatchHandler(header, url, Issue::class)

        return issueHandler.handleRequest() as Issue?
    }
}
