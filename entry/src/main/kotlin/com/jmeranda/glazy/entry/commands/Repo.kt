package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.entry.getRepoName
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Parameters
import picocli.CommandLine.Model.CommandSpec
import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.service.cache.token
import com.jmeranda.glazy.lib.service.RepoService
import displayRepo
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Parent class for all repo commands.
 *
 * @property user The username of the repository owner.
 * @property name The name of the repository.
 * @property token The personal access token
 * @property service The [com.jmeranda.glazy.lib.service.RepoService] to by repo commands.
 */
abstract class RepoCommand {
    abstract val user: String?
    abstract val name: String?
    protected open var token: String? = null
    protected lateinit var service: RepoService

    /**
     * Initialize service to be utilized by repository commands.
     */
    protected open fun initService() {
        this.token = token(this.user ?: return)
        this.service = RepoService(this.token)
    }
}

/**
 * Parent class for repo commands taking optional specification of repository owner and name.
 *
 * @property user The owner of the repository.
 * @property name The name of the repository.
 */
sealed class OptionalRepoCommand : RepoCommand() {
    @Option(names = ["-u", "--user"],
            description = ["The user login for the desired repository."])
    override var user: String? = null

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"])
    override var name: String? = null

    override fun initService() {
        if (this.user == null && this.name == null) {
            val (user, name) = getRepoName()

            this.user = user
            this.name = name
        }

        super.initService()
    }
}

/**
 * Parent class for repo commands taking positional parameters to specify target repository.
 *
 * @property user The owner of the repository.
 * @property name The name of the repository.
 */
sealed class RequiredRepoCommand : RepoCommand() {
    @Parameters(index = "0", description = ["The user login for the desired repository."])
    override lateinit var user: String

    @Parameters(index = "1", description = ["The name of the desired repository"])
    override lateinit var name: String
}

/**
 * Parent for all repo sub-commands.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true)
class RepoParent: Runnable, OptionalRepoCommand() {
    @Spec lateinit var spec: CommandSpec

    /**
     * When run before all child classes, with end program if no sub-command is passed as an argument.
     *
     * @throws CommandLine.ParameterException When no sub-command is entered by terminal.
     */
    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * Display details of a repository to the console.
 *
 * @property fields Optional fields to display along with the default properties.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true)
open class RepoShow : Runnable, RequiredRepoCommand() {
    @Option(names = ["-f", "--fields"],
            description = ["The fields to also show"],
            split = ",")
    private var fields: List<String>? = null

    override fun run() {
        this.initService()

        // Retrieve and display a Repo instance.
        val repo = this.service.getRepo(this.user,
                this.name) ?: return
        displayRepo(repo, fields)
    }
}

/**
 * List all repositories associated with the user.
 *
 * @property user Provides positional parameter to specify the user whose repositories to list.
 */
@Command(name = "list",
        description = ["List names of user repositories."],
        mixinStandardHelpOptions = true)
class RepoList: Runnable, RepoCommand() {
    @Parameters(index = "0", description = ["The user login for the desired repository."])
    override lateinit var user: String

    @Option(names=["--affiliation"],
        description=["The users affiliation to the group (defaults to owner)."])
    var affiliation: String = "owner"

    @Option(names=["--visibility"],
        description = ["The visibility of repositories to show (defaults to all)."])
    var visibility: String? = null

    override val name: String?  = null

    override fun run() {
        this.initService()

        val repoList = this.service.getAllRepos(visibility, affiliation) ?: return

        for (repo: Repo? in repoList) {
            println(repo?.fullName)
        }
    }
}

/**
 * Create a remote repository.
 *
 * @property description The description for the new repository.
 * @property homepage The optional website for the repository.
 * @property private Specifies that the repository is private or public.
 * @property hasIssues Specifies if issues are allowed for the repository.
 * @property hasProjects Specifies that sub-projects are allowed for the repository.
 * @property hasWiki Specifies that wiki is enabled for the repository.
 * @property isTemplate Specifies that the repository is a template.
 * @property teamId The team id to which the repository is the be linked.
 * @property autoInit Create an initial commit with a default README after creation.
 * @property gitignoreTemplate The name of the language whose default gitignore to use.
 * @property licenseTemplate The license for the project.
 * @property allowSquashMerge Allow squash merges.
 * @property allowMergeCommit Allow merge commits.
 * @property allowRebaseMerge Allow rebase merges.
 */
@Command(name = "init",
        description = ["Create a new remote repository"],
        mixinStandardHelpOptions = true)
class RepoInit: Runnable, RequiredRepoCommand() {
    @Option(names = ["-d", "--description"],
            description = ["THe description for the new repository"])
    var description: String? = null

    @Option(names = ["--homepage"],
            description = ["The url to the repositories homepage"])
    var homepage: String? = null

    @Option(names = ["-p", "--private"],
            description = ["Mark the new repository as private."])
    var private: Boolean = false

    @Option(names = ["-i", "--has-issues"],
            description = ["Enable issues for the repository."])
    var hasIssues: Boolean = true
    
    @Option(names = ["--has-project"],
            description = ["Enable projects for the repository."])
    var hasProjects: Boolean = true

    @Option(names = ["-w", "--has-wiki"],
            description = ["Enable wiki for the repository."])
    var hasWiki: Boolean = true

    @Option(names = ["--is-template"],
            description = ["Mark the repository as a template"])
    var isTemplate: Boolean = false

    @Option(names = ["--team-id"],
            description = ["The id of the team to have access to the repository"])
    var teamId: Int? = null

    @Option(names = ["-a", "--auto-init"],
            description = ["Create an initial commit with an empty README"])
    var autoInit: Boolean = false

    @Option(names = ["--gitignore-template"],
            description = ["The language gitignore template to use."])
    var gitignoreTemplate: String? = null

    @Option(names = ["--license-template"],
            description = ["The license to use for the repository (MIT, GPL, etc.)."])
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
        this.initService()

        // Create the remote repository.
        this.service.createRepo(
                this.user,
                this.name,
                this.description,
                this.homepage,
                this.private,
                this.hasIssues,
                this.hasProjects,
                this.hasWiki,
                this.isTemplate,
                this.teamId,
                this.autoInit,
                this.gitignoreTemplate,
                this.licenseTemplate,
                this.allowSquashMerge,
                this.allowMergeCommit,
                this.allowRebaseMerge
        )
    }
}

/**
 * Patch a repository with content specified by class properties.
 *
 * @property newName The new name for the repository.
 * @property description The new description for the  repository.
 * @property homepage The new  homepage for the repository.
 * @property private make the repository private.
 * @property public make the repository public.
 * @property hasIssues Make issues llowed for the repository.
 * @property hasProjects Make sub-projects allowed for the repository.
 * @property hasWiki Enable the wiki for the repository.
 * @property isTemplate Make the repository a template.
 * @property defaultBranch The new default branch for the repository.
 * @property allowSquashMerge Allow squash merges.
 * @property allowMergeCommit Allow merge commits.
 * @property allowRebaseMerge Allow rebase merges.
 * @property archived Archive the repository.
 */
@Command(name = "patch",
        description = ["Edit an existing repository"],
        mixinStandardHelpOptions = true)
class RepoPatch: Runnable, OptionalRepoCommand() {
    @Option(names = ["--new-name"],
            description = ["The new name for the repository."])
    var newName: String? = null

    @Option(names = ["-d", "--description"],
            description = ["THe new description for the repo."])
    var description: String? = null

    @Option(names = ["--homepage"],
            description = ["The new homepage url for the repo."])
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
            description = ["The new default branch for the repo."])
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
        this.initService()
        if (this.user == null || this.name == null) {
            this.initService()
        }

        // Patch the remote repository.
        val repo = this.service.editRepo(this.user ?: return, this.name ?: return,
                this.newName, this.description, this.homepage, this.private ?: this.public?.not(),
                this.hasIssues, this.hasProjects, this.hasWiki, this.isTemplate,
                this.defaultBranch, this.allowSquashMerge, this.allowMergeCommit,
                this.allowRebaseMerge, this.archived) ?: return

        val patchedFields = mutableListOf<String>()
        val ignoreProps = arrayOf("token", "service", "user", "name")

        // Determine which fields have been patched.
        for (prop in RepoPatch::class.memberProperties) {
            if (prop.name in ignoreProps) continue

            if ((prop as KProperty1<RepoPatch, Any>).get(this) != null) {
                if (prop.name == "public"){
                    patchedFields.add("private")
                } else {
                    patchedFields.add(prop.name)
                }
            }
        }

        // Display the repository, specifying the patched values
        displayRepo(repo, patchedFields)
    }
}

/**
 * Delete a remote repository.
 */
@Command(name = "delete",
        description = ["Delete a remote repository, user must have admin privileges."],
        mixinStandardHelpOptions = true)
class RepoDelete: Runnable, RequiredRepoCommand() {

    @Option(names = ["--noconfirm"],
        description = ["Do not ask for confirmation before deleting repository."])
    private var noConfirm: Boolean = false

    /**
     * Get user confirmation to delete a repository.
     *
     * @return True if user confirmed, false otherwise.
     */
    private fun confirm(): Boolean {
        print("Are you sure you would like to delete repository '${this.user}/${this.name}' (Y/n)? ")

        return when (readLine()) {
            "Y", "" -> true
            else -> false
        }
    }

    override fun run() {
        this.initService()

        if (this.noConfirm || this.confirm()) {
            // Delete the remote repository.
            this.service.deleteRepo(this.user, this.name)
        }
    }
}

/**
 * Transfer a remote repository to another user.
 *
 * @property newOwner The username of the new user to own the repository.
 * @property teamIds The ids of teams to transfer the repository into.
 */
@Command(name = "transfer",
        description = ["Transfer a repository to another user."],
        mixinStandardHelpOptions = true)
class RepoTransfer: Runnable, OptionalRepoCommand() {
    @Parameters(index = "0", description = ["The login of the new owner."])
    lateinit var newOwner: String

    @Option(names = ["-t", "--team-ids"],
            description = ["Team id(s) to add to the repository."],
            split = ",")
    lateinit var teamIds: List<Int>

    override fun run() {
        this.initService()

        // Transfer the remote repository.
        this.service.transferRepo(this.user ?: return,this.name ?: return,
                this.newOwner, this.teamIds)
    }
}

/**
 * Create a fork of a repository.
 *
 * @property user The name of the fork owner.
 * @property name The name of the target repository.
 * @property current The name of the target repository owner.
 * @property organization The organization of the fork.
 */
@Command(name = "fork",
        description = ["Create a fork of a repository."],
        mixinStandardHelpOptions = true)
class RepoFork: Runnable, RepoCommand() {
    @Parameters(index = "0", description = ["The new owner for the repository."])
    override lateinit var user: String

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"],
            required = true)
    override lateinit var name: String

    @Option(names = ["-u", "--user"],
            description = ["The owner of the original repository."],
            required = true)
    lateinit var current: String

    @Option(names = ["-o", "--organization"],
            description = ["The name of the organization to fork into."])
    var organization: String? = null

    override fun run() {
        this.initService()

        val repo = this.service.createFork(this.current, this.name, organization)

        displayRepo(repo ?: return, null)
    }
}