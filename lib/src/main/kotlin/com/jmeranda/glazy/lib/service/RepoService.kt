package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoRequest
import com.jmeranda.glazy.lib.handler.RepoGetHandler

/**
 * Client service to construct requests and return response data.
 *
 * @property repo A repository object, defaults to null.
 */
open class RepoService {
    private var repo: Repo? = null

    /**
     * Get a repo with the specified name, and owner.
     *
     * Stores the result for faster access with repeated calls. 
     */
    fun getRepo(name: String, user: String): Repo? {
        if (this.repo != null) { return this.repo }

        val repoRequest = RepoRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest)

        this.repo = repoHandler.handleRequest()

        return this.repo
    }
}