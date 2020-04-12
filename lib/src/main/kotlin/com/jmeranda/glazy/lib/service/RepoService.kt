package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.makeVerbose
import com.jmeranda.glazy.lib.request.*

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Service providing access to repository operations.
 *
 * @param token The access token to be used for authentication.
 */
class RepoService(protected val token: String? = null, verbose: Boolean = false) {
    private val logger: Logger? = if (verbose) {
        Logger.getLogger(RepoService::class.qualifiedName)
            .makeVerbose()
    } else {
        null
    }

    /**
     * Retrieve a remote repository.
     *
     * @param user The owner of the target repository.
     * @param name The name of the target repository.
     * @return The repository owner by [user] and called [name], oro null if repository could not
     *      be found.
     */
    fun getRepo(user: String, name: String): Repo? {
        logger?.log(Level.INFO, "Checking local cache for $user/$name")
        var repo: Repo? = repo(user, name)

        if (repo == null) {
            logger?.log(Level.INFO, "No local cache entry found")
            val request = RepoGetRequest(user, name)
            val header = GlazyTemplateHeader(this.token)
            val repoHandler = GetHandler(header, repoUrl(request), request, Repo::class)

            repo = repoHandler.handleRequest() as Repo?
            write(repo ?: return null)
        }

        return repo
    }

    /**
     * Retrieve a list of all repositories owned by an authenticated user.
     *
     * See https://developer.github.com/v3/repos/#list-your-repositories for details regarding the
     * values of visibility and affiliation.
     *
     * @return The list of found repositories, or null if a user could not be authenticated.
     */
    fun getAllRepos(visibility: String?, affiliation: String?): List<Repo>? {
        val request = RepoGetRequest(String(), String(), visibility, affiliation)
        val header = GlazyVisibilityHeader(this.token)
        val handler = GetHandler(header, currentUserRepoUrlQuery(request), request, Repo::class)

        val list = handler.handleListRequest()
            ?.map { obj -> obj as Repo }

        for (repo in list ?: return null) {
            write(repo)
        }

        return list
    }

    /**
     * Create a remote repository.
     *
     * See https://developer.github.com/v3/repos/#create for detailed explanation of function
     * parameters.
     *
     * @param user
     * @param name
     * @param description
     * @param homepage
     * @param private
     * @param hasIssues
     * @param hasProjects
     * @param hasWiki
     * @param hasWiki
     * @param isTemplate
     * @param teamId
     * @param autoInit
     * @param gitignoreTemplate
     * @param licenseTemplate
     * @param allowSquashMerge
     * @param allowMergeCommit
     * @param allowRebaseMerge
     * @return The new repository, or null if there was an error.
     */
    fun createRepo(
        user: String,
        name: String,
        description: String? = null,
        homepage: String? = null,
        private: Boolean? = null,
        hasIssues: Boolean? = null,
        hasProjects: Boolean? = null,
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
        val request = RepoPostRequest(
            user, name, description, homepage,
            private, hasIssues, hasProjects, hasWiki, isTemplate,
            teamId, autoInit, gitignoreTemplate, licenseTemplate,
            allowSquashMerge, allowMergeCommit, allowRebaseMerge
        )

        val header = GlazySimpleHeader(this.token)
        val repoHandler = PostHandler(header, currentUserRepoUrl(), request, Repo::class)
        val repo = repoHandler.handleRequest() as Repo?

        write(repo ?: return null)

        return repo
    }

    /**
     * Edit a remote repository.
     *
     * See https://developer.github.com/v3/repos/#create for detailed explanation of function
     * parameters.
     *
     * @param user
     * @param name
     * @param description
     * @param homepage
     * @param private
     * @param hasIssues
     * @param hasProjects
     * @param hasWiki
     * @param hasWiki
     * @param isTemplate
     * @param allowSquashMerge
     * @param allowMergeCommit
     * @param allowRebaseMerge
     * @return The edited repository, or null if the target repository could not be found.
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
        val request = RepoPatchRequest(
            user, currentName, name, description,
            homepage, private, hasIssues, hasProjects, hasWiki,
            isTemplate, defaultBranch, allowSquashMerge, allowMergeCommit,
            allowRebaseMerge, archived
        )
        val header = GlazySimpleHeader(this.token)
        val handler = PatchHandler(header, repoUrl(request), request, Repo::class)
        val repo = handler.handleRequest() as Repo?

        write(repo ?: return null)

        return repo
    }

    /**
     * Delete a remote repository.
     *
     * Please ensure that the access token used for authentication has the proper permissions for
     * deleting repositories.
     *
     * @param user The owner of the target repository.
     * @param name The name of the target repository.
     */
    fun deleteRepo(user: String, name: String) {
        val request = RepoDeleteRequest(user, name)
        val header = GlazySimpleHeader(this.token)
        val handler = DeleteHandler(header, repoUrl(request), request, Repo::class)

        handler.handleNoRequest()
    }

    /**
     * Transfer the ownership of a repository.
     *
     * @param user The name of the current owner.
     * @param name The name of the target repository.
     * @param newOwner The name of the new owner.
     * @param teamIds The team ids to add to the repository.
     */
    fun transferRepo(user: String, name: String, newOwner: String, teamIds: List<Int>? = null) {
        val request = RepoTransferRequest(user, name, newOwner, teamIds)
        val header = GlazyTransferableHeader(this.token)
        val handler = PostHandler(header, repoUrl(request), request, Repo::class)

        handler.handleNoRequest()
    }

    /**
     * Create a fork of a repository.
     *
     * @param user The owner of the repository.
     * @param name The name of the repository.
     * @param organization The organization to fork into.
     * @return The fork of the repository, or null if there was an error forking.
     */
    fun createFork(user: String, name: String, organization: String?): Repo? {
        val request = RepoForkRequest(user, name, organization)
        val header = GlazySimpleHeader(this.token)
        val handler = PostHandler(header, forkRootUrl(request), request, Repo::class)
        val repo = handler.handleRequest() as Repo?

        write(repo ?: return null)

        return repo
    }

    /**
     * Create a repository from a template.
     */
    fun fromTemplate(
        templateOwner: String,
        templateName: String,
        user: String? = null,
        name: String? = null,
        description: String? = null,
        private: Boolean? = null
    ): Repo? {
        val request = RepoTemplateRequest(
            user ?: getUser() ?: return null,
            name ?: templateName,
            templateOwner, templateName,
            description,
            private)
        val header = GlazyTemplateHeader(this.token)
        val handler = PostHandler(header, templateUrl(request), request, Repo::class)
        val repo = handler.handleRequest() as Repo?

        write(repo ?: return null)

        return repo
    }
}