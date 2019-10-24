package com.jmeranda.glazy.cli.commands

import com.jmeranda.glazy.cli.getRepoName

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Parameters
import picocli.CommandLine.Model.CommandSpec
import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.RepoService
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
abstract class RepoCommand {
    abstract val user: String?
    abstract val name: String?
    protected open var token: String? = null
    protected lateinit var service: RepoService

    /**
     * Set the value of the private token.
     */
    protected fun getCachedToken() {
        this.token = CacheService.token(this.user ?: return)
    }

    /**
     * Set the value of the private service.
     */
    protected abstract fun startService()
}

sealed class OptionalRepoCommand : RepoCommand() {
    @Option(names = ["-u", "--user"],
            description = ["The user login for the desired repository."])
    override var user: String? = null

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"])
    override var name: String? = null

    override fun startService() {
        if (this.user == null && this.name == null) {
            val (user, name) = getRepoName()

            this.user = user
            this.name = name
        }

        this.getCachedToken()
        this.service = RepoService(this.token)
    }
}

sealed class RequiredRepoCommand : RepoCommand() {
    @Parameters(index = "0", description = ["The user login for the desired repository."])
    override lateinit var user: String

    @Parameters(index = "1", description = ["The name of the desired repository"])
    override lateinit var name: String

    override var token: String? = null

    override fun startService() {
        this.getCachedToken()
        this.service = RepoService(this.token)
    }
}

/**
 * Parent for all repo sub-commands.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true)
class RepoParent : Runnable, OptionalRepoCommand() {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * Sub-command to show information about a repository specified via the
 * [user] and [name] options.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true)
open class RepoShow : Runnable, OptionalRepoCommand() {
    @Option(names = ["-f", "--fields"],
            description = ["The fields to also show"],
            split = ",")
    private var fields: List<String>? = null

    override fun run() {
        this.startService()

        if (this.user == null || this.name == null) {
            this.startService()
        }

        // Retrieve and display a Repo instance.
        val repo = this.service.getRepo(this.user ?: return,
                this.name?: return) ?: return
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

        this.getCachedToken()
        this.startService()

        if (this.user == null) {
            this.startService()
        }

        val repoList = this.service.getAllRepos() ?: return

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
    var hasProject: Boolean = true

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
        this.getCachedToken()
        this.startService()

        // Create the remote repository.
        this.service.createRepo(
                this.user,
                this.name,
                this.description,
                this.homepage,
                this.private,
                this.hasIssues,
                this.hasProject,
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
        this.getCachedToken()
        this.startService()
        if (this.user == null || this.name == null) {
            this.startService()
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
 * Delete a remote repository owned by [user] and called [name].
 */
@Command(name = "delete",
        description = ["Delete a remote repository, user must have admin privileges."],
        mixinStandardHelpOptions = true)
class RepoDelete: Runnable, OptionalRepoCommand() {
    override fun run() {
        this.getCachedToken()
        this.startService()
        if (this.user == null || this.name == null) {
            this.startService()
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
class RepoTransfer: Runnable, OptionalRepoCommand() {
    @Parameters(index = "0", description = ["The login of the new owner."])
    lateinit var newOwner: String

    @Option(names = ["-t", "--team-ids"],
            description = ["Team id(s) to add to the repository."],
            split = ",")
    lateinit var teamIds: List<Int>

    override fun run() {
        this.getCachedToken()
        this.startService()

        // Transfer the remote repository.
        this.service.transferRepo(this.user ?: return,this.name ?: return,
                this.newOwner, this.teamIds)
    }
}