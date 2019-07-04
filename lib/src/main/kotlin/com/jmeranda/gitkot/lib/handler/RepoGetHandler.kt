package com.jmeranda.gitkot.lib.handler

import khttp.get
import com.beust.klaxon.Klaxon

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest

class RepoGetHandler(repoRequest: RepoRequest): Handler {
    override var request: RepoRequest = repoRequest
    private val repositoryUrl: String? = Handler.endpoints?.repositoryUrl

    override fun handleRequest(): Repo? {
        val repoAsJson: String = get("$repositoryUrl/${request.user}/${request.repo}").text
        return Klaxon().parse<Repo>(repoAsJson)
    }
}
