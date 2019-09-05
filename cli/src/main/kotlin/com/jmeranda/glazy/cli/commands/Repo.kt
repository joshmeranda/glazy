package com.jmeranda.glazy.cli.commands

import com.jmeranda.glazy.cli.getRepoName

import picocli.CommandLine.Option
import picocli.CommandLine.Command

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Display information about the given [repo].
 */
fun displayRepo(repo: Repo) {
    println("name: ${repo.name}")
    println("owner: ${repo.owner.login}")
    println("private: ${repo.private}")
    println("created: ${repo.createdAt}")
    println("clone url: ${repo.cloneUrl}")
}

/**
 * Class to be inherited by all sub-classes to RepoParent
 *
 * Despite not being a 'lateinit' (since it may be null) treat [token] a lateinit.
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

    private val cache: CacheService = CacheService()
    private var token: String? = null
    protected lateinit var service: RepoService

    /**
     * Use the values parsed from the current or parent repository directory, if either is null.
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

    protected fun setToken() {
        this.token = CacheService.token(this.user ?: return)
    }

    protected fun setService() {
        this.service = RepoService(this.token)
    }
}

/**
 * Parent command for all repo operations.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true)
class RepoParent

/**
 * Sub-command to show information about a repository.
 *
 * If no [user] or [name] arguments are passed as arguments, the values
 * parsed in the glazy command are used to show the current repo.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true)
open class RepoShow: Runnable, RepoCommand() {
    override fun run() {
        this.setToken()
        this.setService()

        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        val repo = this.service.getRepo(this.user ?: return,
                this.name?: return)
        displayRepo(repo)
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

        val repoList = this.service.getAllRepos(this.user ?: return)

        for (repo: Repo? in repoList) {
            println(repo?.fullName)
        }
    }
}

/**
 * Sub-command used to create a remote repository.
 *
 * TODO document properties
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

        this.service.createRepo(this.name ?: return, this.description,
                this.homepage, this.private, this.hasIssues,
                this.hasProject, this.hasWiki, this.isTemplate,
                this.teamId, this.autoInit, this.gitignoreTemplate,
                this.licenseTemplate, this.allowSquashMerge,
                this.allowMergeCommit, this.allowRebaseMerge)
    }
}

/**
 * TODO document
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

        this.service.editRepo(this.user ?: return , this.name ?: return,
                this.newName, this.description, this.homepage, this.private ?: this.public?.not(),
                this.hasIssues, this.hasProjects, this.hasWiki, this.isTemplate,
                this.defaultBranch, this.allowSquashMerge, this.allowMergeCommit,
                this.allowRebaseMerge, this.archived)
    }
}

/**
 * TODO document
 */
@Command(name = "delete",
        description = ["Delete a remote repository."],
        mixinStandardHelpOptions = true)
class RepoDelete: Runnable, RepoCommand() {
    override fun run() {
        this.setToken()
        this.setService()
        if (this.user == null || this.name == null) {
            this.useDefaultRepoInfo()
        }

        this.service.deleteRepo(this.user ?: return,
                this.name ?: return)
    }
}

/**
 * TODO document
 */
@Command(name = "transfer",
        description = ["Transfer a repository to another user."],
        mixinStandardHelpOptions = true)
class RepoTransfer(): Runnable, RepoCommand() {
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

        this.service.transferRepo(this.user ?: return,this.name ?: return,
                this.newOwner!!, this.teamIds)
    }
}