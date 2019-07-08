package com.jmeranda.gitkot.lib.handler

import khttp.get

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest

class RepoGetHandler(private val repoRequest: RepoRequest): Handler() {
    private val repositoryUrl: String = Handler.endpoints.repositoryUrl

    override fun handleRequest(): Repo? {
        val repoAsJson: String = get(this.getRequestUrl()).text
        var repo: Repo?

        try {
            repo = Handler.fieldRenameKlaxon.parse<Repo>(repoAsJson)
        } catch( e:  Exception) {
            repo = null
        }

        return repo
    }

    override fun getRequestUrl(): String = this.repositoryUrl
            .replace("{owner}", this.repoRequest.owner)
            .replace("{repo}", this.repoRequest.name)
}
