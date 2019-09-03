package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.Repo

import com.jmeranda.glazy.lib.exception.BadRequest
import com.jmeranda.glazy.lib.exception.NoSuchIssue

import com.jmeranda.glazy.lib.handler.IssueAllGetHandler
import com.jmeranda.glazy.lib.handler.IssueGetHandler
import com.jmeranda.glazy.lib.handler.IssuePatchHandler
import com.jmeranda.glazy.lib.handler.IssuePostHandler
import com.jmeranda.glazy.lib.request.IssueGetAllRequest
import com.jmeranda.glazy.lib.request.IssueGetRequest
import com.jmeranda.glazy.lib.request.IssuePatchRequest
import com.jmeranda.glazy.lib.request.IssuePostRequest

/**
 * Client service to construct requests and return response data.
 */
class IssueService(
        private val repo: Repo,
        private val token: String?
) {
    /**
     * Get a specific issue by number from the repository.
     *
     * @param number The number of the issue.
     * @return The issue received from the api.
     */
    fun getIssue(number: Int): Issue {
        val issueRequest = IssueGetRequest(this.repo, number)
        val issueHandler = IssueGetHandler(issueRequest, this.token)

        return issueHandler.handleRequest() ?: throw NoSuchIssue(number)
    }

    /**
     * Get every issue in the current repository.
     *
     *  @return A list of all the issues received from the api.
     */
    fun getAllIssues(): List<Issue> {
        val issueAllRequest = IssueGetAllRequest(this.repo)
        val issueAllGetHandler = IssueAllGetHandler(issueAllRequest, this.token)

        return issueAllGetHandler.handleRequest() ?: throw Exception()
    }

    /**
     * Create a new issue in the repository.
     *
     * @param title The name of the new issue.
     * @param body The body of the new issue.
     * @param milestone The number of the milestone this issue is
     *        associated with.
     * @param labels The labels for the new issue.
     * @param assignees The login ids for the users to assign to the
     *        issue.
     * @return The issue created by the request.
     */
    fun createIssue(title: String, body: String?, milestone: Int?,
                    labels: List<String>?, assignees: List<String>?
    ): Issue {
        val issueRequest = IssuePostRequest(this.repo, title, body, milestone, labels, assignees)
        val issueHandler = IssuePostHandler(issueRequest, this.token)

        return issueHandler.handleRequest() ?: throw BadRequest("Could not create issue.")
    }

    /**
     * Edit an already existing issue.
     *
     * @param number The number of the issue to be edited.
     * @param title The name of the issue.
     * @param body The body of the issue.
     * @param state The state of the issue, defaults to
     *        open if not state specified.
     * @param milestone The number of the milestone this issue is
     *        associated with.
     * @param labels The labels for the issue.
     * @param assignees The login ids for the users to assign to the issue.
     * @return The issue edited by the request.
     */
    fun editIssue(number: Int, title: String?, body: String?, state: String?,
                  milestone: Int?, labels: List<String>?, assignees: List<String>?
    ): Issue {
        val issueRequest = IssuePatchRequest(this.repo, number, title,
                body, state, milestone, labels, assignees)
        val issueHandler = IssuePatchHandler(issueRequest, number, this.token)

        return issueHandler.handleRequest() ?: throw BadRequest()
    }
}
