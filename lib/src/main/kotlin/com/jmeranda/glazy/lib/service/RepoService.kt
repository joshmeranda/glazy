package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.request.*

/**
 * Client service to construct requests and return response data.
 * @property token The personal access token for the user
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
     * Get all repos owned by a user.
     */
    fun getAllRepos(user: String): List<Repo> {
        val request = RepoAllGetRequest(user)
        val handler = RepoAllGetHandler(request, token)

        return handler.handleRequest() ?: throw Exception("No such user ${request.user}")
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

    fun transferRepo(name: String, newOwner: String, teamIds: List<Int>?) {
        val request = RepoTransferRequest(name, newOwner, teamIds)
        val handler = RepoTransferHandler(request, this.token)

        return handler.handleRequest()
    }
}