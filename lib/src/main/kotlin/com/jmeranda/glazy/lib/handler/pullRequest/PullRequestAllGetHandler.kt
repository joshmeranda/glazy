package com.jmeranda.glazy.lib.handler.pullRequest

import com.fasterxml.jackson.module.kotlin.readValue

import khttp.get
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.handler.GlazyDraftableHeader
import com.jmeranda.glazy.lib.handler.GlazySimplePullUrl
import com.jmeranda.glazy.lib.handler.Handler

class PullRequestAllGetHandler(
        header: GlazyDraftableHeader,
        url: GlazySimplePullUrl
): Handler(header, url) {
    override fun handleRequest(): List<PullRequest>? {
        val headers = this.getHeaders()
        val response: Response = get(this.requestUrl, headers = headers)
        var pullRequest: List<PullRequest>? = null

        if (! handleCode(response)) return null

        try {
            pullRequest = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pullRequest
    }
}