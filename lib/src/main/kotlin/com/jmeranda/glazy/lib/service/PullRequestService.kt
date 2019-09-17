package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.handler.*
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
}