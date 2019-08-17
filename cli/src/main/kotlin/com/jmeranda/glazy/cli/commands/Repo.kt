package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Option
import picocli.CommandLine.Command
import picocli.CommandLine.ParentCommand

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NotInRepo
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
 * Class to be inherited by all sub-classes to RepoParent.
 */
open class RepoCommand() {
    @Option(names = ["-u", "--user"],
            description = ["The user login for the desired repository."],
            paramLabel = "LOGIN"
    )
    var user: String? = null

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"],
            paramLabel = "NAME"
    )
    var name: String? = null
}

/**
 * Parent command for all repo operations.
 *
 * @property parent Reference to the parent command instance.
 * @property service The repo service used to interact with the github api.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true
)
class RepoParent(): Runnable {
    @ParentCommand
    val parent: Glazy? = null
    lateinit var service: RepoService

    /**
     * Instantiate the service required by sub-commands.
     *
     * Any sub-command which requires the use of an issue service will
     * be required to call this method or service erro will be thrown.
     */
    override fun run() {
        this.parent?.run()
        this.service = this.parent?.repoService ?: return
    }
}

/**
 * Sub-command to show information about a repository.
 *
 * If no [user] or [name] arguments are passed as arguments, the values
 * parsed in the glazy command are used to show the current repo.
 *
 * @property parent Reference to the parent command instance.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true
)
 class RepoShow(): Runnable, RepoCommand() {
    @ParentCommand
    private val parent: RepoParent? = null

    override fun run() {
        this.parent?.run()
        /* use the value parsed in the Glazy parent command if non
         * passed as argument */
        if (this.user == null) { this.user = this.parent?.parent?.user }
        if (this.name == null) { this.name = this.parent?.parent?.name }

        if (this.name == null || this.user == null) { throw NotInRepo() }

        val repo = this.parent?.service?.getRepo(this.name?: return,
                this.user ?: return)
        displayRepo(repo ?: return)
    }
}

/**
 * Sub-command used to create a remote repository.
 *
 * TODO document properties
 */
@Command(name = "init",
        description = ["Create a new remot repository"],
        mixinStandardHelpOptions = true)
class RepoInit(): Runnable {
    @ParentCommand
    private val parent: RepoParent? = null

    @Option(names=["-n", "--name"],
            description = ["The name for the new repository."],
            paramLabel = "NAME",
            required = true)
    var name: String = ""

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
        this.parent?.run()

        this.parent?.service?.createRepo(this.name, this.description,
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
class RepoPatch(): Runnable, RepoCommand() {
    @ParentCommand
    private val parent: RepoParent? = null

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
        this.parent?.run()
        if (this.user == null) { this.user = this.parent?.parent?.user }
        if (this.name == null) { this.name = this.parent?.parent?.name }

        if (this.user == null || this.name == null) { throw NotInRepo() }

        this.parent?.service?.editRepo(this.user ?: return , this.name ?: return,
                this.newName, this.description, this.homepage, this.private ?: this.public?.not(),
                this.hasIssues, this.hasProjects, this.hasWiki, this.isTemplate,
                this.defaultBranch, this.allowSquashMerge, this.allowMergeCommit,
                this.allowRebaseMerge, this.archived)
    }
}


@Command(name = "delete",
        description = ["Delete a remote repository."],
        mixinStandardHelpOptions = true)
class RepoDelete(): Runnable, RepoCommand() {
    @ParentCommand
    private val parent: RepoParent? = null

    override fun run() {
        this.parent?.run()
        this.parent?.service?.deleteRepo(this.user ?: return, 
                this.name ?: return)
    }
}

class RepoTransfer() {
}