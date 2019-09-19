package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.handler.pullRequest.PullRequestAllGetHandler
import com.jmeranda.glazy.lib.handler.pullRequest.PullRequestGetHandler
import com.jmeranda.glazy.lib.handler.pullRequest.PullRequestPostHandler
import com.jmeranda.glazy.lib.handler.pullRequest.PullRequestPatchHandler
import com.jmeranda.glazy.lib.request.*

class PullRequestService(
        private val user: String,
        private val name: String,
        private val token: String?
) {
    /**
     * Get list of all pull requests for the repository.
     */
    fun getAllPullRequests(): List<PullRequest> {
        val request = PullGetAllRequest(this.user, this.name)
        val handler = PullRequestAllGetHandler(request, this.token)

        return handler.handleRequest() ?: throw Exception("Could not authenticate using specified token")
    }

    /**
     * Get the pull request with the specified [number].
     */
    fun getPullRequest(number: Int): PullRequest {
        val request = PullGetRequest(this.user, this.name, number)
        val handler = PullRequestGetHandler(request, this.token)

        return handler.handleRequest() ?: throw Exception("Could not authenticate using specified token")
    }

    /**
     * Create a pull request with the specified [title] or [issue], [head],
     * [base], [body], [canModify], and [draft].
     */
    fun createPullRequest(title:String?, issue: Int?, head: String,
                          base: String, body: String?, canModify: Boolean?,
                          draft: Boolean?
    ): PullRequest {
        val request = PullPostRequest(user, name, title, issue, head, base, body, canModify, draft)
        val handler = PullRequestPostHandler(request, this.token)

        return handler.handleRequest() ?: throw Exception("An error occurred")
    }

    fun patchPullRequest(number: Int, title: String?, body: String?, state: String?,
                          base: String?, canModify: Boolean?): PullRequest {
        val request = PullPatchRequest(this.user, this.name, number, title, body, state, base, canModify)
        val handler = PullRequestPatchHandler(request, this.token)

        return handler.handleRequest() ?: throw Exception("An error occurred")
    }
}