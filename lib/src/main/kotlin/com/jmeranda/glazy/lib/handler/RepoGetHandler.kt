package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoGetRequest
import com.jmeranda.glazy.lib.service.CacheService

/**
 * Handle request for a repository.
 *
 * @property request The repository request.
 * @property token The personal access token of the user.
 */
class RepoGetHandler(
        private val request: RepoGetRequest,
        token: String? = null
): Handler<Repo>(token) {
    private val repositoryUrl: String = endpoints.repositoryUrl

    /**
     * Send the request and return the response repository.
     *
     * @return Repo? The repository object received from the API, null
     *     if there are json parsing errors.
     */
    override fun handleRequest(): Repo? {
        /* Initialize repo from cached value */
        var repo: Repo? = CacheService.repo(request.user, request.name)

        /* TODO Move conditional outside of try block by returning string from cache*/
        try {
            if (repo == null) {
                val response = get(this.getRequestUrl(), headers = this.getAuthorizationHeaders())
                handleCode(response.statusCode)
                repo = mapper.readValue(response.text)
            }
        } catch(e:  Exception) {
            repo = null
            e.printStackTrace()
        }

        if (repo != null) CacheService.write(repo)

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}
