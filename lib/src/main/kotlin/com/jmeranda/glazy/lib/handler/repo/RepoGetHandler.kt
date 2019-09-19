package com.jmeranda.glazy.lib.handler.repo

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.request.RepoGetRequest
import com.jmeranda.glazy.lib.service.CacheService

/**
 * Handle a [request] for a repository using the specified [token].
 */
class RepoGetHandler(
        private val request: RepoGetRequest,
        token: String? = null
): Handler<Repo>(token) {
    private val repositoryUrl: String = endpoints.repositoryUrl

    override fun handleRequest(): Repo? {
        /* Initialize repo from cached value */
        var repo: Repo? = CacheService.repo(request.user, request.name)

        /* TODO Move conditional outside of try block by returning string from cache*/
        try {
            if (repo == null) {
                val response = get(this.getRequestUrl(), headers = this.getAuthorizationHeaders())

                if (!handleCode(response)) return null

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

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}
