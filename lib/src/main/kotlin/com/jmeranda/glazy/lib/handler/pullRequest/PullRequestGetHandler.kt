package com.jmeranda.glazy.lib.handler.pullRequest

import com.fasterxml.jackson.module.kotlin.readValue

import khttp.get
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.handler.GlazyDraftableHeader
import com.jmeranda.glazy.lib.handler.GlazyPullUrl
import com.jmeranda.glazy.lib.handler.Handler

class PullRequestGetHandler(
        header: GlazyDraftableHeader,
        url: GlazyPullUrl
): Handler(header, url) {
    override fun handleRequest(): PullRequest? {
        val headers = this.getHeaders()
        val response: Response = get(this.requestUrl, headers = headers)
        var pullRequest: PullRequest? = null

        if (! handleCode(response)) return null

        try {
            pullRequest = Handler.mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pullRequest
    }
}