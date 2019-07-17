package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoRequest
import khttp.get

/**
 * Handle request for a repository.
 *
 * @property repoRequest The repository request.
 */
class RepoGetHandler(
        private val repoRequest: RepoRequest,
        token: String? = null
): Handler(token) {
    private val repositoryUrl: String = Handler.endpoints.repositoryUrl

    /**
     * Send the request and return the response repository.
     *
     * @return Repo? The repository object received from the API, null
     * if there are json parsing errors.
     */
    override fun handleRequest(): Repo? {
        var repo: Repo? = Handler.cache.repo(repoRequest.name, repoRequest.owner)
        if (repo != null) { return repo }

        val repoAsJson: String =
                get(this.getRequestUrl(), headers=this.getAuthorizationHeaders()).text

        try {
            repo = Handler.fieldRenameKlaxon.parse<Repo>(repoAsJson)
        } catch(e:  Exception) {
            repo = null
        }

        if (repo != null) { Handler.cache.write(repo) }

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.repoRequest.owner)
            .replace("{repo}", this.repoRequest.name)
}
