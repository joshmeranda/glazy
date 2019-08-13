package com.jmeranda.glazy.lib.handler

import com.fasterxml.jackson.module.kotlin.readValue

import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoPostRequest

/**
 * Handle a POST request for a new repository.
 *
 * @property repoRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class RepoPostHandler(
        private val repoRequest: RepoPostRequest,
        token: String?
): Handler<Repo>(token) {
    private val repoUrl = endpoints.currentUserRepositoriesUrl

    override fun handleRequest(): Repo? {
        var body: String? = null

        try {
            body = Handler.writer().writeValueAsString(this.repoRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders()
        )

        return Handler.reader().readValue(response.text)
    }

    /**
     * Remove trailing optional parameter specifications.
     */
    override fun getRequestUrl(): String = this.repoUrl
            .replace(Regex("\\{.*}"), "")
}