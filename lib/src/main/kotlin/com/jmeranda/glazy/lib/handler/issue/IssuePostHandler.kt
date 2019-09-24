package com.jmeranda.glazy.lib.handler.issue

import khttp.post
import khttp.responses.Response

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazySimpleIssueUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Issue

/**
 * Handle a [request] to create a new repository issue using the specified [token].
 */
class IssuePostHandler(
        header: GlazySimpleHeader,
        url: GlazySimpleIssueUrl
): Handler(header, url) {
    override fun handleRequest(): Issue? {
        var body: String? = null

        // Deserialize the request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (!handleCode(response)) return null

        var issue: Issue? = null

        // Serialize the received json.
        try {
            issue = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return issue
    }
}