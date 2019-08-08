package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.RepoGetHandler
import com.jmeranda.glazy.lib.request.RepoGetRequest

/**
 * Client service to construct requests and return response data.
 * @property repo A repository object, defaults to null.
 */
open class RepoService(
        private var token: String?
){
    private lateinit var repo: Repo

    /**
     * Get a repo with the specified name, and owner.
     */
    fun getRepo(name: String, user: String): Repo {
        val repoRequest = RepoGetRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest, token)

        this.repo = repoHandler.handleRequest() ?: throw NoSuchRepo(name)

        return this.repo
    }
}