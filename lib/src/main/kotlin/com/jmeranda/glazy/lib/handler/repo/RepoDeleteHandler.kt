package com.jmeranda.glazy.lib.handler.repo

import com.jmeranda.glazy.lib.handler.GlazyRepoUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import khttp.delete
import khttp.responses.Response

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoDeleteRequest

/**
 * Handle a [request] to delete a repository using the specified [token].
 * Be aware that if the authenticated user does not have admin permissions
 * to the repository, it will not be deleted.
 */
class RepoDeleteHandler(
        header: GlazySimpleHeader,
        url: GlazyRepoUrl
): Handler(header, url) {
    override fun handleRequest() {
        val response: Response = delete(this.requestUrl,
                headers = this.getHeaders())

        if (! handleCode(response)) return
    }
}