package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoGetRequest

/**
 * Handle request for a repository.
 * @property repoRequest The repository request.
 * @property token The personal access token of the user.
 */
class RepoGetHandler(
        private val repoRequest: RepoGetRequest,
        token: String? = null
): Handler<Repo>(token) {
    private val repositoryUrl: String = Handler.endpoints.repositoryUrl

    /**
     * Send the request and return the response repository.
     * @return Repo? The repository object received from the API, null
     *     if there are json parsing errors.
     */
    override fun handleRequest(): Repo? {
//        var repo: Repo? = Handler.cache.repo(repoRequest.name, repoRequest.owner)
//        if (repo != null) { return repo }
        var repo: Repo?

        val repoAsJson: String =
                get(this.getRequestUrl(), headers=this.getAuthorizationHeaders()).text

        try {
            repo = Handler.reader().readValue(repoAsJson)
        } catch(e:  Exception) {
            repo = null
            e.printStackTrace()
        }

        if (repo != null) { Handler.cache.write(repo) }

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.repoRequest.owner)
            .replace("{repo}", this.repoRequest.name)
}
