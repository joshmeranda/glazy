package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.Issue
import khttp.post

import com.jmeranda.glazy.lib.request.IssuePostRequest
import khttp.responses.Response

/**
 * Handle a POST request for a new issue.
 * @property issueRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssuePostHandler(
        private val issueRequest: IssuePostRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): Issue? {
        var body: String? = null

        try {
            body = Handler.fieldRenameKlaxon.toJsonString(this.issueRequest)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.getRequestUrl(),
                data = body,
                headers = this.getAuthorizationHeaders()
                )

        return Handler.fieldRenameKlaxon.parse(response.text)
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("{/number}","")
}