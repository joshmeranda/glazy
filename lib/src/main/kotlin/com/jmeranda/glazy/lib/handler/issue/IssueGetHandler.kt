package com.jmeranda.glazy.lib.handler.issue

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyIssueUrl

import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Issue

/**
 * Handle a [request] for a specific repository issue using the specified [token].
 */
class IssueGetHandler(
        header: GlazySimpleHeader,
        url: GlazyIssueUrl
): Handler(header, url) {
    override fun handleRequest(): Issue? {
        val response = get(this.requestUrl, headers=this.getHeaders())
        var issue: Issue? = null

        if (! handleCode(response)) return null

        // Serialize received json.
        try {
            issue = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return issue
    }
}