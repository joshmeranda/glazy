package com.jmeranda.glazy.cli.commands

import com.jmeranda.glazy.cli.getRepoName

import picocli.CommandLine.Option
import picocli.CommandLine.Command

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.RepoService
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Display information about the given [repo], with optional additional [fields].
 */
fun displayRepo(repo: Repo, fields: List<String>?) {
    var details = "full name: ${repo.fullName}\n" +
            "private: ${repo.private}\n" +
            "created: ${repo.createdAt}\n" +
            "clone url: ${repo.cloneUrl}"

    val badFields = mutableListOf<String>()

    // Concatenate additional fields to the details string.
    for (field in fields ?: listOf()) {
        // If property exists in class add to details, if not add to badFields.
        try {
            // Get repo property via input fields.
            val property = repo::class
                    .memberProperties
                    .first { it.name == field }
                    as? KProperty1<Repo, Any>
            // Print the field name and value to the console.
            if (property != null) details += "\n$field: ${property.get(repo)}"
        } catch (e: Exception) {
            badFields.add(field)
        }
    }

    // Notify user of unrecognized fields
    if (badFields.size > 0) details += "\n\nglazy: Could not recognize field(s) '${badFields.joinToString()}'.\n" +
            "Please see 'https://developer.github.com/v3/repos/#list-your-repositories' for available fields"

    println(details)
}

/**
 * Class to provide the [token] and [service] issue commands, as well
 * as [user] and [name] options. Despite not being a 'lateinit' (since
 * it may be null) treat [token] as a lateinit. Due to this please ensure
 * that setToken and setService are called before either property is used.
 */
open class RepoCommand {
    @Option(names = ["-u", "--user"],
            description = ["The user login for the desired repository."],
            paramLabel = "LOGIN")
    open var user: String? = null

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"],
            paramLabel = "NAME")
    open var name: String? = null

    private var token: String? = null
    protected lateinit var service: RepoService

    /**
     * Use the values parsed from the current or parent repository
     * directory, if either is null.
     */
    fun useDefaultRepoInfo() {
        val (user, name) = getRepoName()

        this.user = user
        this.name = name

        /* If not in a repository directory or sub-directory */
        if (this.user == null && this.name == null) { throw NotInRepo() }

        this.setToken()
        this.setService()
    }

    /**
     * Set the value of the private token.
     */
    protected fun setToken() {
        this.token = CacheService.token(this.user ?: return)
    }

    /**
     * Set the value of the private service.
     */
    protected fun setService() {
        this.service = RepoService(this.token)
    }
}

/**
 * Parent for all repo sub-commands.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true)
class RepoParent

/**
 * Sub-command to show information about a repository specified via the
 * [user] and [name] options.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true)
open class RepoShow: Runnable, RepoCommand() {
    @Option(names = ["-f", "--fields"],
            description = ["The fields to also show"],
            split = ",",
            paramLabel = "FIELD")
    private var fields: List<String>? = null

    override fun run() {
        this.setToken()
        this.setService()

        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        // Retrieve and display a Repo instance.
        val repo = this.service.getRepo(this.user ?: return,
                this.name?: return)
        displayRepo(repo, fields)
    }
}

@Command(name = "list",
        description = ["List names of user repositories."],
        mixinStandardHelpOptions = true)
class RepoList: RepoShow() {
    override fun run() {
        if (this.name != null) {
            super.run()
            return
        }

        this.setToken()
        this.setService()

        if (this.user == null) {
            this.useDefaultRepoInfo()
        }

        val repoList = this.service.getAllRepos()

        for (repo: Repo? in repoList) {
            println(repo?.fullName)
        }
    }
}

/**
 * Create a remote repository with content specified by class properties.
 */
@Command(name = "init",
        description = ["Create a new remote repository"],
        mixinStandardHelpOptions = true)
class RepoInit: Runnable, RepoCommand() {
    @Option(names = ["-d", "--description"],
            description = ["THe description for the new repository"],
            paramLabel = "DESCRIPTION")
    var description: String? = null

    @Option(names = ["--homepage"],
            description = ["The url to the repositories homepage"],
            paramLabel = "URL")
    var homepage: String? = null

    @Option(names = ["-p", "--private"],
            description = ["Mark the new repository as private."])
    var private: Boolean = false

    @Option(names = ["-i", "--has-issues"],
            description = ["Enable issues for the repository."])
    var hasIssues: Boolean = true
    
    @Option(names = ["--has-project"],
            description = ["Enable projects for the repository."])
    var hasProject: Boolean = true

    @Option(names = ["-w", "--has-wiki"],
            description = ["Enable wiki for the repository."])
    var hasWiki: Boolean = true

    @Option(names = ["--is-template"],
            description = ["Mark the repository as a template"])
    var isTemplate: Boolean = false

    @Option(names = ["--team-id"],
            description = ["The id of the team to have access to the repository"],
            paramLabel = "ID")
    var teamId: Int? = null

    @Option(names = ["-a", "--auto-init"],
            description = ["Create an initial commit with an empty README"])
    var autoInit: Boolean = false

    @Option(names = ["--gitignore-template"],
            description = ["The language gitignore template to use."],
            paramLabel = "LANG")
    var gitignoreTemplate: String? = null

    @Option(names = ["--license-template"],
            description = ["The license to use for the repository (MIT, GPL, etc.)."],
            paramLabel = "LICENSE")
    var licenseTemplate: String? = null

    @Option(names = ["--allow-squash"],
            description = ["Allow squash merging pull requests"])
    var allowSquashMerge: Boolean = true

    @Option(names = ["--allow_merge"],
            description = ["Allow merging pull requests with a commit."])
    var allowMergeCommit: Boolean = true

    @Option(names = ["--allow-rebase"],
            description = ["Allow rebase merging pull requests."])
    var allowRebaseMerge: Boolean = true

    override fun run() {
        this.setToken()
        this.setService()
        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        // Create the remote repository.
        this.service.createRepo(this.name ?: return, this.description,
                this.homepage, this.private, this.hasIssues,
                this.hasProject, this.hasWiki, this.isTemplate,
                this.teamId, this.autoInit, this.gitignoreTemplate,
                this.licenseTemplate, this.allowSquashMerge,
                this.allowMergeCommit, this.allowRebaseMerge)
    }
}

/**
 * Patch a repository with content specified by class properties.
 */
@Command(name = "patch",
        description = ["Edit an existing repository"],
        mixinStandardHelpOptions = true)
class RepoPatch: Runnable, RepoCommand() {
    @Option(names = ["--new-name"],
            description = ["The new name for the repository."],
            paramLabel = "NAME")
    var newName: String? = null

    @Option(names = ["-d", "--description"],
            description = ["THe new description for the repo."],
            paramLabel = "DESCRIPTION")
    var description: String? = null

    @Option(names = ["--homepage"],
            description = ["The new homepage url for the repo."],
            paramLabel = "URL")
    var homepage: String? = null

    @Option(names = ["--private"],
            description = ["Make the repository private."])
    var private: Boolean? = null

    @Option(names = ["--public"],
            description = ["Make the repository public."])
    var public: Boolean? = null

    @Option(names = ["--has-issues"],
            description = ["Allow issue for the repo."])
    var hasIssues: Boolean? = null

    @Option(names = ["--has-projects"],
            description = ["Allow projects for the repo."])
    var hasProjects: Boolean? = null

    @Option(names = ["--has-wiki"],
            description = ["Allow wiki for the repo."])
    var hasWiki: Boolean? = null

    @Option(names = ["--is-tempalte"],
            description = ["Mark the repository as a template"])
    var isTemplate: Boolean? = null

    @Option(names = ["-b", "--default-branch"],
            description = ["The new default branch for the repo."],
            paramLabel = "BRANCH")
    var defaultBranch: String? = null

    @Option(names = ["--allow-squash"],
            description = ["Allow squash merging for the repo."])
    var allowSquashMerge: Boolean? = null

    @Option(names = ["--allow-merge"],
            description = ["Allow merge commits for the repo."])
    var allowMergeCommit: Boolean? = null

    @Option(names = ["--allow-rebase"],
            description = ["Allow rebase merges for the repo."])
    var allowRebaseMerge: Boolean? = null

    @Option(names = ["-a", "--archive"],
            description = ["Archive the repo."])
    var archived: Boolean? = null

    override fun run() {
        this.setToken()
        this.setService()
        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        // Patch the remote repository.
        this.service.editRepo(this.user ?: return , this.name ?: return,
                this.newName, this.description, this.homepage, this.private ?: this.public?.not(),
                this.hasIssues, this.hasProjects, this.hasWiki, this.isTemplate,
                this.defaultBranch, this.allowSquashMerge, this.allowMergeCommit,
                this.allowRebaseMerge, this.archived)
    }
}

/**
 * Delete a remote repository owned by [user] and called [name].
 */
@Command(name = "delete",
        description = ["Delete a remote repository, user must have admin privileges."],
        mixinStandardHelpOptions = true)
class RepoDelete: Runnable, RepoCommand() {
    override fun run() {
        this.setToken()
        this.setService()
        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        // Delete the remote repository.
        this.service.deleteRepo(this.user ?: return,
                this.name ?: return)
    }
}

/**
 * Transfer a remote repository owned by [user] called [name], to a new
 * user [newOwner] with the optional team ids [teamIds].
 */
@Command(name = "transfer",
        description = ["Transfer a repository to another user."],
        mixinStandardHelpOptions = true)
class RepoTransfer: Runnable, RepoCommand() {
    @Option(names = ["-o", "--new-owner"],
            description = ["The login of the new owner."],
            paramLabel = "LOGIN",
            required = true)
    var newOwner: String? = null

    @Option(names = ["-t", "--team-ids"],
            description = ["Team id(s) to add to the repository."],
            split = ",",
            paramLabel = "IDS")
    var teamIds: List<Int>? = null

    override fun run() {
        this.setToken()
        this.setService()

        // Transfer the remote repository.
        this.service.transferRepo(this.user ?: return,this.name ?: return,
                this.newOwner!!, this.teamIds)
    }
}