package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.handler.RepoGetHandler
import com.jmeranda.glazy.lib.request.RepoGetRequest

/**
 * Client service to construct requests and return response data.
 * @property repo A repository object, defaults to null.
 */
open class RepoService(
        private var token: String?
){
    private var repo: Repo? = null

    /**
     * Get a repo with the specified name, and owner.
     * Stores the result for faster access with repeated calls.
     */
    fun getRepo(name: String, user: String): Repo? {
        if (this.repo != null) { return this.repo }

        val repoRequest = RepoGetRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest, token)

        this.repo = repoHandler.handleRequest()

        return this.repo
    }
}