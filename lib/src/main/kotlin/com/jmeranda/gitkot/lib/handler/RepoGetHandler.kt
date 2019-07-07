package com.jmeranda.gitkot.lib.handler

import khttp.get
import khttp.responses.Response

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest

import com.jmeranda.gitkot.lib.handler.Handler

class RepoGetHandler(repoRequest: RepoRequest): Handler() {
    private val request: RepoRequest = repoRequest
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

    override fun getRequestUrl(): String {
        val requestUrl: String = this.repositoryUrl

        requestUrl.replace("{owner}", this.request.owner)
        requestUrl.replace("{repo}", this.request.name)

        return requestUrl
    }
}
