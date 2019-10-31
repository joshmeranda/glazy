package com.jmeranda.glazy.lib.service

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.handler.fork.ForkPostHandler
import com.jmeranda.glazy.lib.handler.repo.*
import com.jmeranda.glazy.lib.request.*

/**
 * Class to run operations on remote repositories using [token] for authentication.
 */
class RepoService(private var token: String?){
    /**
     * Get a repo with the specified [name], and [user].
     */
    fun getRepo(user: String, name: String): Repo? {
        var repo = CacheService.repo(user, name)

        if (repo == null) {
            val request = RepoGetRequest(user, name)
            val header = GlazySimpleHeader(this.token)
            val url = GlazyRepoUrl(request)
            val repoHandler = GetHandler(header, url, Repo::class)

            repo = repoHandler.handleRequest() as Repo
        }

        return repo
    }

    /**
     * Get all repos owned by a user.
     */
    fun getAllRepos(): List<Repo>? {
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCurrentUserRepoUrl()
        val handler = GetHandler(header, url, Repo::class)
        val mapper: ObjectMapper = jacksonObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        var data: List<Repo>? = null
        try {
            data = mapper.readValue(handler.handleListRequest())
        } catch (e: JsonMappingException) {
            println("Error mapping api response.")
        }

        return data
    }

    /**
     * Create a remote repository.
     */
    fun createRepo(
            user: String,
            name: String,
            description: String? = null,
            homepage: String? = null,
            private: Boolean? = null,
            hasIssues: Boolean? = null,
            hasProject: Boolean? = null,
            hasWiki: Boolean? = null,
            isTemplate: Boolean? = null,
            teamId: Int? = null,
            autoInit: Boolean? = null,
            gitignoreTemplate: String? = null,
            licenseTemplate: String? = null,
            allowSquashMerge: Boolean? = null,
            allowMergeCommit: Boolean? = null,
            allowRebaseMerge: Boolean? = null
    ): Repo? {
        val request = RepoPostRequest(user, name, description, homepage,
                private, hasIssues, hasProject, hasWiki, isTemplate,
                teamId, autoInit, gitignoreTemplate, licenseTemplate,
                allowSquashMerge, allowMergeCommit, allowRebaseMerge)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCurrentUserRepoUrl(request)
        val repoHandler = PostPatchHandler(header, url, Repo::class)

        return repoHandler.handleRequest() as Repo?
    }

    /**
     * Edit a remote repository.
     */
    fun editRepo(
            user: String,
            currentName: String,
            name: String? = null,
            description: String? = null,
            homepage: String? = null,
            private: Boolean? = null,
            hasIssues: Boolean? = null,
            hasProjects: Boolean? = null,
            hasWiki: Boolean? = null,
            isTemplate: Boolean? = null,
            defaultBranch: String? = null,
            allowSquashMerge: Boolean? = null,
            allowMergeCommit: Boolean? = null,
            allowRebaseMerge: Boolean? = null,
            archived: Boolean? = null
    ): Repo? {
        val request = RepoPatchRequest(user, currentName, name, description,
                homepage, private, hasIssues, hasProjects, hasWiki,
                isTemplate, defaultBranch, allowSquashMerge, allowMergeCommit,
                allowRebaseMerge, archived)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyRepoUrl(request)
        val handler = PostPatchHandler(header, url, Repo::class)

        return handler.handleRequest() as Repo?
    }

    /**
     * Delete the remote repository called [name] owned by [user].
     */
    fun deleteRepo(user: String, name: String) {
        val request = RepoDeleteRequest(user, name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyRepoUrl(request)
        val handler = DeleteHandler(header, url, Repo::class)

        handler.handleRequest() as Repo?
    }

    /**
     * Transfer the ownership of the repository called [name] from
     * [user] fo [newOwner] with optional [teamIds].
     */
    fun transferRepo(user: String, name: String, newOwner: String, teamIds: List<Int>? = null) {
        val request = RepoTransferRequest(user, name, newOwner, teamIds)
        val header = GlazyTransferableHeader(this.token)
        val url = GlazyRepoUrl(request)
        val handler = TransferHandler(header, url, Repo::class)

        handler.handleRequest() as Repo?
    }

    /**
     * Create a fork of  repository called [name] and owned by [user].
     */
    fun createFork(user: String, name: String, organization: String?): Repo? {
        val request = RepoForkRequest(user, name, organization)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyForkUrl(request)
        val handler = PostPatchHandler(header, url, Repo::class)

        return handler.handleRequest() as Repo?
    }
}