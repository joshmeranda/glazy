package com.jmeranda.gitkot.lib.service

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest
import com.jmeranda.gitkot.lib.handler.RepoGetHandler

/**
 * Client service to construct requests and return response data.
 *
 * @property name name of the repository to get.
 * @property user name of the repository owner.
 */
open class RepoService(
        protected val name: String,
        protected val user: String
) {
    private fun getRepo(): Repo? {
        val repoRequest: RepoRequest = RepoRequest(this.name, this.user)
        val repoHandler: RepoGetHandler = RepoGetHandler(repoRequest)

        return repoHandler.getRepo()
    }
}