package com.jmeranda.glazy.lib.handler

import khttp.post

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoPostRequest
import khttp.responses.Response

/**
 * Handle a POST request for a new repository.
 *
 * @property repoRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class RepoPostHandler(
        private val repoRequest: RepoPostRequest,
        token: String?
): Handler(token) {
    private val repoUrl = endpoints.currentUserRepositoriesUrl

    override fun handleRequest(): Repo? {
        var body: String? = null

        try {
            body = Handler.fieldRenameKlaxon.toJsonString(this.repoRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders()
        )

        return Handler.fieldRenameKlaxon.parse(response.text)
    }

    /**
     * Remove trailing optional parameter specifications.
     */
    override fun getRequestUrl(): String = this.repoUrl
            .replace(Regex("\\{.*}"), "")
}