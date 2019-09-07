package com.jmeranda.glazy.lib.handler

import com.fasterxml.jackson.module.kotlin.readValue

import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoPostRequest

/**
 * Handle a [request] to create a remote repository using the specified [token].
 */
class RepoPostHandler(
        private val request: RepoPostRequest,
        token: String?
): Handler<Repo>(token) {
    private val repoUrl = endpoints.currentUserRepositoriesUrl

    override fun handleRequest(): Repo? {
        var body: String? = null

        // Deserialize the request.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders()
        )

        handleCode(response.statusCode)

        var repo: Repo? = null

        // Serialize received json.
        try {
            repo = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return repo
    }

    /**
     * Remove trailing optional parameter specifications.
     */
    override fun getRequestUrl(): String = this.repoUrl
            .replace(Regex("\\{.*}"), "")
}