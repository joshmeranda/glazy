package com.jmeranda.glazy.lib.handler.pullRequest

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyDraftableHeader
import com.jmeranda.glazy.lib.handler.GlazyPullUrl
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.request.PullPatchRequest
import khttp.patch
import khttp.responses.Response

class PullRequestPatchHandler(
        header: GlazyDraftableHeader,
        url: GlazyPullUrl
): Handler(header, url) {
    override fun handleRequest(): PullRequest? {
        var body: String? = null

        // Deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Prepare and send post request to api
        val headers = this.getHeaders()
        val response: Response = patch(this.requestUrl,
                data  = body,
                headers = headers)

        if (! handleCode(response)) return null

        var pullRequest: PullRequest? = null

        // Serialize the received json.
        try {
            pullRequest = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pullRequest
    }
}