package com.jmeranda.glazy.lib.handler.repo

import com.jmeranda.glazy.lib.handler.GlazyRepoUrl
import com.jmeranda.glazy.lib.handler.GlazyTransferableHeader
import com.jmeranda.glazy.lib.handler.Handler
import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoTransferRequest

/**
 * Handle a [request] to transfer ownership of a repository using the
 * specified [token].
 */
class RepoTransferHandler(
        header: GlazyTransferableHeader,
        url: GlazyRepoUrl
): Handler(header, url) {
    override val requestUrl: String = super.requestUrl
            .plus("/transfer")

    override fun handleRequest() {
        var body: String? = null
        val headers = this.getHeaders()

        // Deserialize the request.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.requestUrl,
                data = body,
                headers = headers)

        handleCode(response)
    }
}