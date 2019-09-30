package com.jmeranda.glazy.lib.handler.issue

import khttp.get
import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.Issue

/**
 * Handle a [request] for all repository issues using the specified [token].
 */
class IssueAllGetHandler(
        header: GlazySimpleHeader,
        url: GlazySimpleIssueUrl
): Handler(header, url) {
    override fun handleRequest(): List<Issue>? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        var allIssues: List<Issue>? = null

        if (! handleCode(response)) return null

        // Serialize the received json.
        try {
            allIssues = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allIssues
    }
}