package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.request.*

/**
 * Class to run operations on remote repositories using [token] for authentication.
 */
class RepoService(private var token: String?){
    /**
     * Get a repo with the specified [name], and [user].
     */
    fun getRepo(user: String, name: String): Repo {
        val repoRequest = RepoGetRequest(user, name)
        val repoHandler = RepoGetHandler(repoRequest, token)

        return repoHandler.handleRequest() ?: throw NoSuchRepo(name)
    }


    /**
     * Get all repos owned by a user.
     */
    fun getAllRepos(): List<Repo> {
        val handler = RepoAllGetHandler(token ?: String())

        return handler.handleRequest() ?: throw Exception("Could not authenticate using specified token")
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
    fun editRepo(user: String, currentName: String, name: String?,
                 description: String?, homepage: String?, private: Boolean?,
                 hasIssues: Boolean?, hasProjects: Boolean?, hasWiki: Boolean?,
                 isTemplate: Boolean?, defaultBranch: String?, allowSquashMerge: Boolean?,
                 allowMergeCommit: Boolean?, allowRebaseMerge: Boolean?,
                 archived: Boolean?
    ): Repo {
        val request = RepoPatchRequest(user, currentName, name, description,
                homepage, private, hasIssues, hasProjects, hasWiki,
                isTemplate, defaultBranch, allowSquashMerge, allowMergeCommit,
                allowRebaseMerge, archived)
        val handler = RepoPatchHandler(request, this.token)

        return handler.handleRequest() ?: throw NoSuchRepo(currentName)
    }

    /**
     * Delete the remote repository called [name] owned by [user].
     */
    fun deleteRepo(user: String, name: String) {
        val request = RepoDeleteRequest(user, name)
        val handler = RepoDeleteHandler(request, this.token)

        return handler.handleRequest()
    }

    /**
     * Transfer the ownership of the repository called [name] from
     * [user] fo [newOwner] with optional [teamIds].
     */
    fun transferRepo(user: String, name: String, newOwner: String, teamIds: List<Int>?) {
        val request = RepoTransferRequest(user, name, newOwner, teamIds)
        val handler = RepoTransferHandler(request, this.token)

        return handler.handleRequest()
    }
}