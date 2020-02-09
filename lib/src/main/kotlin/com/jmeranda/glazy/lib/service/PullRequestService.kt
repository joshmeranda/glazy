package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.request.PullGetAllRequest
import com.jmeranda.glazy.lib.request.PullGetRequest
import com.jmeranda.glazy.lib.request.PullPatchRequest
import com.jmeranda.glazy.lib.request.PullPostRequest

/**
 * Service providing access to operations on pull requests.
 *
 * @see [Service]
 */
class PullRequestService(user: String, name: String, token: String?): Service(user, name, token) {
    /**
     * Get list of all pull requests for the repository.
     */
    fun getAllPullRequests(): List<PullRequest>? {
        val request = PullGetAllRequest(this.user, this.name)
        val header = GlazyDraftableHeader(this.token)
        val handler = GetHandler(header, pullRootUrl(request), request, PullRequest::class)

        return handler.handleListRequest()
            ?.map{ obj -> obj as PullRequest }
    }

    /**
     * Get the pull request with the specified [number].
     */
    fun getPullRequest(number: Int): PullRequest? {
        val request = PullGetRequest(this.user, this.name, number)
        val header = GlazyDraftableHeader(this.token)
        val handler = GetHandler(header, pullUrl(request), request, PullRequest::class)

        return handler.handleRequest() as PullRequest?
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
        val handler = PostHandler(header, pullRootUrl(request), request, PullRequest::class)

        return handler.handleRequest() as PullRequest?
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
        val handler = PatchHandler(header, pullUrl(request), request, PullRequest::class)

        return handler.handleRequest() as PullRequest?
    }
}