package com.jmeranda.glazy.lib.handler.issue

import khttp.patch
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.handler.GlazyIssueUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Issue

/**
 * Handle a [request] to patch a repository issue using the specified [token].
 */
class IssuePatchHandler(
        header: GlazySimpleHeader,
        url: GlazyIssueUrl
): Handler(header, url) {
    override fun handleRequest(): Issue? {
        var body: String? = null

        // Deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (!handleCode(response)) return null

        var issue: Issue? = null

        // Serialize received json.
        try {
            issue = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return issue
    }
}