package com.jmeranda.glazy.lib.handler

import khttp.patch
import khttp.responses.Response

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssuePatchRequest

/**
 * Handle a PATCH request to edit an issue.
 *
 * @property issueRequest The request object used by the handler.
 * @property number The number of the issue to edit.
 * @property token The personal access token of the user.
 */
class IssuePatchHandler(
        private val issueRequest: IssuePatchRequest,
        private val number: Int,
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

        val response: Response = patch(this.issueUrl,
                data = body,
                headers = this.getAuthorizationHeaders())

        return Handler.fieldRenameKlaxon.parse(response.text)
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("/number", "/$number")
}