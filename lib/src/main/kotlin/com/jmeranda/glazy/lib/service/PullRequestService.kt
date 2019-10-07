package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.GlazyDraftableHeader
import com.jmeranda.glazy.lib.handler.GlazyPullUrl
import com.jmeranda.glazy.lib.handler.GlazySimplePullUrl
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
    fun getAllPullRequests(): List<PullRequest>? {
        val request = PullGetAllRequest(this.user, this.name)
        val header = GlazyDraftableHeader(this.token)
        val url = GlazySimplePullUrl(request)

        val handler = PullRequestAllGetHandler(header, url)

        return handler.handleRequest()
    }

    /**
     * Get the pull request with the specified [number].
     */
    fun getPullRequest(number: Int): PullRequest? {
        val request = PullGetRequest(this.user, this.name, number)
        val header = GlazyDraftableHeader(this.token)
        val url = GlazyPullUrl(request)

        val handler = PullRequestGetHandler(header, url)

        return handler.handleRequest()
    }

    /**
     * Create a pull request with the specified [title] or [issue], [head],
     * [base], [body], [canModify], and [draft].
     */
    fun createPullRequest(
            title: String? = null,
            head: String,
            base: String,
            issue: Int? = null,
            body: String? = null,
            canModify: Boolean? = null,
            draft: Boolean? = null
    ): PullRequest? {
        val request = PullPostRequest(user, name, title, issue, head, base, body, canModify, draft)
        val header = GlazyDraftableHeader(this.token)
        val url = GlazySimplePullUrl(request)
        val handler = PullRequestPostHandler(header, url)

        return handler.handleRequest()
    }

    fun patchPullRequest(
            number: Int,
            title: String? = null,
            body: String? = null,
            state: String? = null,
            base: String? = null,
            canModify: Boolean? = null
    ): PullRequest? {
        val request = PullPatchRequest(this.user, this.name, number, title, body, state, base, canModify)
        val header = GlazyDraftableHeader(this.token)
        val url = GlazyPullUrl(request)

        val handler = PullRequestPatchHandler(header, url)

        return handler.handleRequest()
    }
}