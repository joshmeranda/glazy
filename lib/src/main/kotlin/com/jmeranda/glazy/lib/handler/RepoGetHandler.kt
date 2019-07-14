package com.jmeranda.glazy.lib.handler

import khttp.get

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoRequest

class RepoGetHandler(private val repoRequest: RepoRequest): Handler() {
    private val repositoryUrl: String = Handler.endpoints.repositoryUrl

    override fun handleRequest(): Repo? {
        var repo: Repo? = Handler.cache.repo(repoRequest.name, repoRequest.owner)
        if (repo != null) { return repo }

        val repoAsJson: String = get(this.getRequestUrl()).text

        try {
            repo = Handler.fieldRenameKlaxon.parse<Repo>(repoAsJson)
        } catch(e:  Exception) {
            repo = null
        }

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.repoRequest.owner)
            .replace("{repo}", this.repoRequest.name)
}
