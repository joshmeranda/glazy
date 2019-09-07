package com.jmeranda.glazy.lib.handler

import khttp.delete
import khttp.responses.Response

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoDeleteRequest

/**
 * Handle a [request] to delete a repository using the specified [token].
 * Be aware that if the authenticated user does not have admin permissions
 * to the repository, it will not be deleted.
 */
class RepoDeleteHandler(
        private val request: RepoDeleteRequest,
        token: String?
): Handler<Repo>(token) {
    override fun handleRequest() {
        val response: Response = delete(this.getRequestUrl(),
                headers = this.getAuthorizationHeaders())

        handleCode(response.statusCode)

        // TODO will be replaced in later update.
        if (response.statusCode != 204) {
            println(response.jsonObject.get("message"))
        } else {
            println("deleted")
        }
    }

    override fun getRequestUrl(): String = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}