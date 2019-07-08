package com.jmeranda.gitkot.lib.service

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest
import com.jmeranda.gitkot.lib.handler.RepoGetHandler

/**
 * Client service to construct requests and return response data.
 */
open class RepoService {
    private var repo: Repo? = null

    fun getRepo(name: String, user: String): Repo? {
        if (this.repo != null) { return this.repo }

        val repoRequest = RepoRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest)

        this.repo = repoHandler.handleRequest()

        return this.repo
    }
}