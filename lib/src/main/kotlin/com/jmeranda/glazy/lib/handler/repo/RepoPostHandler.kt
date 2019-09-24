package com.jmeranda.glazy.lib.handler.repo

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyCurrentUserRepoUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler

import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoPostRequest

/**
 * Handle a [request] to create a remote repository using the specified [token].
 */
class RepoPostHandler(
        header: GlazySimpleHeader,
        url: GlazyCurrentUserRepoUrl
): Handler(header, url) {
    override fun handleRequest(): Repo? {
        var body: String? = null

        // Deserialize the request.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.requestUrl,
                data = body,
                headers = this.getHeaders()
        )

        if (! handleCode(response)) return null

        var repo: Repo? = null

        // Serialize received json.
        try {
            repo = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return repo
    }
}