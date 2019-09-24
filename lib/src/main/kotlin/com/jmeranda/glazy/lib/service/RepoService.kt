package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.GlazyCurrentUserRepoUrl
import com.jmeranda.glazy.lib.handler.GlazyRepoUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazyTransferableHeader
import com.jmeranda.glazy.lib.handler.repo.*
import com.jmeranda.glazy.lib.request.*

/**
 * Class to run operations on remote repositories using [token] for authentication.
 */
class RepoService(private var token: String?){
    /**
     * Get a repo with the specified [name], and [user].
     */
    fun getRepo(user: String, name: String): Repo {
        val request = RepoGetRequest(user, name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyRepoUrl(request)
        val repoHandler = RepoGetHandler(header, url)

        return repoHandler.handleRequest() ?: throw NoSuchRepo(name)
    }


    /**
     * Get all repos owned by a user.
     */
    fun getAllRepos(): List<Repo> {
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCurrentUserRepoUrl()

        val handler = RepoAllGetHandler(header, url)

        return handler.handleRequest() ?: throw Exception("Could not authenticate using specified token")
    }

    /**
     * Create a remote repository.
     */
    fun createRepo(user: String, name: String, description: String?, homepage: String?,
                   private: Boolean, hasIssues: Boolean, hasProject: Boolean,
                   hasWiki: Boolean, isTemplate: Boolean, teamId: Int?,
                   autoInit: Boolean, gitignoreTemplate: String?,
                   licenseTemplate: String?, allowSquashMerge: Boolean,
                   allowMergeCommit: Boolean, allowRebaseMerge: Boolean
    ): Repo {
        val request = RepoPostRequest(user, name, description, homepage,
                private, hasIssues, hasProject, hasWiki, isTemplate,
                teamId, autoInit, gitignoreTemplate, licenseTemplate,
                allowSquashMerge, allowMergeCommit, allowRebaseMerge)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCurrentUserRepoUrl()

        val repoHandler = RepoPostHandler(header, url)

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
        val header = GlazySimpleHeader(this.token)
        val url = GlazyRepoUrl(request)

        val handler = RepoPatchHandler(header, url)

        return handler.handleRequest() ?: throw NoSuchRepo(currentName)
    }

    /**
     * Delete the remote repository called [name] owned by [user].
     */
    fun deleteRepo(user: String, name: String) {
        val request = RepoDeleteRequest(user, name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyRepoUrl(request)

        val handler = RepoDeleteHandler(header, url)

        return handler.handleRequest()
    }

    /**
     * Transfer the ownership of the repository called [name] from
     * [user] fo [newOwner] with optional [teamIds].
     */
    fun transferRepo(user: String, name: String, newOwner: String, teamIds: List<Int>?) {
        val request = RepoTransferRequest(user, name, newOwner, teamIds)
        val header = GlazyTransferableHeader(this.token)
        val url = GlazyRepoUrl(request)

        val handler = RepoTransferHandler(header, url)

        return handler.handleRequest()
    }
}