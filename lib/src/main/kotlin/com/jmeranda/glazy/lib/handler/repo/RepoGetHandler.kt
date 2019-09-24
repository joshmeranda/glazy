package com.jmeranda.glazy.lib.handler.repo

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyRepoUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoGetRequest
import com.jmeranda.glazy.lib.service.CacheService

/**
 * Handle a [request] for a repository using the specified [token].
 */
class RepoGetHandler(
        header: GlazySimpleHeader,
        url: GlazyRepoUrl
): Handler(header, url) {
    override fun handleRequest(): Repo? {
        // Initialize repo from cached value
        var repo: Repo? = null

        if (this.request != null) {
            repo = CacheService.repo(this.request.user, this.request.name)
        }

        /* TODO Move conditional outside of try block by returning string from cache*/
        try {
            if (repo == null) {
                val response = get(this.requestUrl, headers = this.getHeaders())

                if (! handleCode(response)) return null

                // Serialize     the received json.
                repo = mapper.readValue(response.text)
            }
        } catch(e:  Exception) {
            e.printStackTrace()
        }

        // Cache the repository if received from the api.
        // TODO check if received from api rather than simply null
        if (repo != null) CacheService.write(repo)

        return repo
    }
}
