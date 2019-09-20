package com.jmeranda.glazy.lib.handler.repo

import com.jmeranda.glazy.lib.handler.Handler
import khttp.delete
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoDeleteRequest

/**
 * Handle a [request] to delete a repository using the specified [token].
 * Be aware that if the authenticated user does not have admin permissions
 * to the repository, it will not be deleted.
 */
class RepoDeleteHandler(
        private val request: RepoDeleteRequest,
        token: String?
): Handler(token) {
    override fun handleRequest() {
        val response: Response = delete(this.getRequestUrl(),
                headers = this.getAuthorizationHeaders())

        if (! handleCode(response)) return
    }

    override fun getRequestUrl(): String = endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}