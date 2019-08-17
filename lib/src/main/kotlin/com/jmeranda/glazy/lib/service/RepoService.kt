package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.RepoDeleteHandler
import com.jmeranda.glazy.lib.handler.RepoGetHandler
import com.jmeranda.glazy.lib.handler.RepoPatchHandler
import com.jmeranda.glazy.lib.handler.RepoPostHandler
import com.jmeranda.glazy.lib.request.RepoDeleteRequest
import com.jmeranda.glazy.lib.request.RepoGetRequest
import com.jmeranda.glazy.lib.request.RepoPatchRequest
import com.jmeranda.glazy.lib.request.RepoPostRequest

/**
 * Client service to construct requests and return response data.
 * @property repo A repository object, defaults to null.
 */
open class RepoService(
        private var token: String?
){
    /**
     * Get a repo with the specified [name], and [user].
     */
    fun getRepo(name: String, user: String): Repo {
        val repoRequest = RepoGetRequest(name, user)
        val repoHandler = RepoGetHandler(repoRequest, token)

        return repoHandler.handleRequest() ?: throw NoSuchRepo(name)
    }

    /**
     * Create a remote repository.
     */
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

    /**
     * Edit a remote repository.
     */
    fun editRepo(owner: String, currentName: String, name: String?,
                 description: String?, homepage: String?, private: Boolean?,
                 hasIssues: Boolean?, hasProjects: Boolean?, hasWiki: Boolean?,
                 isTemplate: Boolean?, defaultBranch: String?, allowSquashMerge: Boolean?,
                 allowMergeCommit: Boolean?, allowRebaseMerge: Boolean?,
                 archived: Boolean?
    ): Repo {
        val request = RepoPatchRequest(owner, currentName, name, description,
                homepage, private, hasIssues, hasProjects, hasWiki,
                isTemplate, defaultBranch, allowSquashMerge, allowMergeCommit,
                allowRebaseMerge, archived)
        val handler = RepoPatchHandler(request, this.token)

        return handler.handleRequest()
    }

    /**
     * Delete a remote repository.
     */
    fun deleteRepo(owner: String, name: String) {
        val request = RepoDeleteRequest(name, owner)
        val handler = RepoDeleteHandler(request, this.token)

        return handler.handleRequest()
    }
}