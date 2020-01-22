package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.request.IssueGetAllRequest
import com.jmeranda.glazy.lib.request.IssueGetRequest
import com.jmeranda.glazy.lib.request.IssuePatchRequest
import com.jmeranda.glazy.lib.request.IssuePostRequest

/**
 * Service providing access to operating on repository issues.
 *
 * @see [Service]
 */
class IssueService(user: String, name: String, token: String?): Service(user, name, token) {
    /**
     * Retrieve a specific repository issue.
     *
     * @param number The number  of the issue to retrieve.
     * @return The issue with the given [number], or null if not found.
     */
    fun getIssue(number: Int): Issue? {
        val request = IssueGetRequest(this.user, this.name, number)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyIssueUrl(request)
        val handler = GetHandler(header, url, Issue::class)

        return handler.handleRequest() as Issue?
    }

    /**
     * Retrieve all issues from a repository.
     *
     * @return All issues from the target repository, or null if repository not found.
     */
    fun getAllIssues(): List<Issue>? {
        val request = IssueGetAllRequest(this.user, this.name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleIssueUrl(request)
        val handler = GetHandler(header, url, Issue::class)

        return handler.handleListRequest()
            ?.map{ obj -> obj as Issue }
    }

    /**
     * Create and return a new issue in the repository given the issue's
     * [title], [body], [milestone], [labels], and [assignees].
     *
     * @param title The title for the issue.
     * @param body The body content of the issue.
     * @param milestone The milestone to be associated with the issue.
     * @param labels The labels to mark the issue.
     * @param assignees The assignees for the issue.
     * @return The newly created issue, or null if the issue  could not be created.
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
     * Edit a existing repository issue.
     *
     * @param number The number of the target issue.
     * @param title The new title for the issue.
     * @param body The new body content of the issue.
     * @param state The new state of the issue (open or closedd).
     * @param milestone The new milestone to be assocaited with the issue,
     * @param labels The new labels to mark the issue.
     * @param assignees The new assignees to tbe put on the issue.
     * @return The edited issue, or null if the issue could not be found or edited.
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
