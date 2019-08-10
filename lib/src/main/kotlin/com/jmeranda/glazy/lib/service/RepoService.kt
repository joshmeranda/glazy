package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.RepoGetHandler
import com.jmeranda.glazy.lib.handler.RepoPostHandler
import com.jmeranda.glazy.lib.request.RepoGetRequest
import com.jmeranda.glazy.lib.request.RepoPostRequest

/**
 * Client service to construct requests and return response data.
 * @property repo A repository object, defaults to null.
 */
open class RepoService(
        private var token: String?
){
    /**
     * Get a repo with the specified name, and owner.
     */
    fun getRepo(name: String, user: String): Repo {
        val repoRequest = RepoGetRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest, token)

        return repoHandler.handleRequest() ?: throw NoSuchRepo(name)
    }

    fun createRepo(name: String, description: String?, homepage: String?,
                   private: Boolean, hasIssues: Boolean, hasProject: Boolean,
                   hasWiki: Boolean, isTemplate: Boolean, teamId: Int?,
                   autoInit: Boolean, gitignoreTemplate: String?,
                   licenseTemplate: String?, allowSquashMerge: Boolean,
                   allowMergeCommit: Boolean, allowRebaseMerge: Boolean
    ): Repo {
        val repoRequest = RepoPostRequest(name, description, homepage,
                private, hasIssues, hasProject, hasWiki, isTemplate,
                teamId, autoInit, gitignoreTemplate, licenseTemplate,
                allowSquashMerge, allowMergeCommit, allowRebaseMerge)
        val repoHandler = RepoPostHandler(repoRequest, token)

        return repoHandler.handleRequest() ?: throw Exception("Could not create repository")
    }
}