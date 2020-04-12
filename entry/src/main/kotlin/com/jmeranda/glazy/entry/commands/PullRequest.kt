package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.entry.Verbose
import picocli.CommandLine
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Command
import picocli.CommandLine.Mixin
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.PullRequestService
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.service.getToken
import com.jmeranda.glazy.lib.service.getRepoName

/**
 * Parent class for all pull request commands.
 *
 * @property service The [com.jmeranda.glazy.lib.service.PullRequestService] utilized by label commands.
 * @property token The token to use for api authentication.
 */
open class PullCommand {
    @Mixin
    var verbose: Verbose? = null
    protected lateinit var service: PullRequestService
    private var token: String? = null

    protected fun initService() {
        val (user, name) = getRepoName()
        if (user != null) token = getToken()

        if (name != null && user != null) this.service = PullRequestService(user, name, token)
        Handler.verbose = this.verbose?.verbose ?: false
    }
}

/**
 * Parent for all pull request sub-commands.
 */
@Command(name = "pull",
        description = ["Perform operations on pull requests"],
        mixinStandardHelpOptions = true)
class PullParent: Runnable {
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
 * List pull requests.
 *
 * @property number The number of the pull request to show.
 * @property fields The specific fields of the pull request to show.
 */
@Command(name = "list",
        description = ["List pull requests"],
        mixinStandardHelpOptions = true)
class PullList: Runnable, PullCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the desired pull request."])
    private val number: Int? = null

    @Option(names = ["-f", "--fields"],
            description = ["The fields to also show"],
            split = ",")
    private var fields: List<String>? = null

    override fun run() {
        this.initService()
        // Retrieve pull request list
        val pullList: List<PullRequest?>? = if (this.number == null) {
            this.service.getAllPullRequests()
        } else {
            listOf(this.service.getPullRequest(this.number))
        }

        for (pull: PullRequest? in pullList ?: listOf()) {
            displayPullRequest(pull ?: continue, fields)
        }
    }
}

/**
 * Create a new pull request.
 *
 * @property exclusive Argument group which specifies either the title for the new pull request or
 *      the issue from which it is to be initiated.
 * @property head The head branch which is to be merged.
 * @property base The branch into which the [head] branch is to be merged.
 * @property body The body of the pull requested.
 * @property canModify Specifies whether the pull requests is read-only or not to repository
 *      maintainers.
 * @property draft Specifies that the pull request is a work in progress (WIP).
 */
@Command(name = "init",
        description = ["Create a merge request."],
        mixinStandardHelpOptions = true)
class PullInit: Runnable, PullCommand() {
    @ArgGroup(exclusive = true, multiplicity = "1")
    private lateinit var exclusive: PullGroup

    @Parameters(index = "0", description = ["The branch to merge."])
    private lateinit var head: String

    @Parameters(index = "1", description = ["The branch to merge the head branch into."])
    private lateinit var base: String

    @Option(names = ["-b", "--body"],
            description = ["The contents of the pull request."])
    private var body: String? = null

    @Option(names = ["-m", "--can-modify"],
            description = ["Specifies that the repository maintainers can modify the pull request."])
    private var canModify: Boolean? = null

    @Option(names = ["-d", "--draft"],
            description = ["Specifies whether the pull request is a draft or not."])
    private var draft: Boolean? = null

    override fun run() {
        this.initService()
        val pullRequest = this.service.createPullRequest(
                this.exclusive.title,
                this.head,
                this.base,
                this.exclusive.issue,
                this.body,
                this.canModify,
                this.draft
        ) ?: return
        displayPullRequest(pullRequest, listOf())
    }

    companion object {
        /**
         * Class to provide the ability to create a pull request from an issue, or use a title.
         *
         * @property title The title for the new pull request.
         * @property issue The number of the issue to attatch to the pull request.
         */
        class PullGroup {
            @Option(names = ["-t", "--title"],
                    description = ["The title for the pull request."],required = true)
            lateinit var title: String

            @Option(names = ["-i", "--issue"],
                    description = ["The issue number to create the pull request from."],
                    required = true)
            var issue: Int = -1 // Will be overridden but cannot be lateinit
        }
    }
}

/**
 * Edit an existing pull request.
 *
 * @property number The number of the pull request which is to be modified.
 * @property title The new title for the pull request.
 * @property body The new body for the pull request.
 * @property state The new state for the pull request.
 * @property base The new base branch for the pull request.
 * @property canModify Specifies whether the pull requests is read-only or not to repository
 *      maintainers.
 */
@Command(name = "patch",
        description = ["Send a patch to a pull request."],
        mixinStandardHelpOptions = true)
class PullUpdate: Runnable, PullCommand() {
    @Parameters(index = "0", description=["The number of the desired pull request."])
    private var number: Int = -1 // Will be overridden but cannot be lateinit

    @Option(names = ["-t", "--title"],
            description = ["The new title for the path request."])
    private var title: String? = null

    @Option(names = ["-b", "--body"],
            description = ["The new body for the pull request."])
    private var body: String? = null

    @Option(names = ["-s", "--state"],
            description = ["THe new state for the pull request."])
    private var state: String? = null

    @Option(names = ["--base"],
            description = ["The new branch to merge into."])
    private var base: String? = null

    @Option(names = ["-m", "--can-modify"],
            description = ["Specify that the maintainer can modify the request."])
    private var canModify: Boolean? = null

    override fun run() {
        this.initService()
        val pullRequest = this.service.patchPullRequest(this.number,
                this.title,
                this.body,
                this.state,
                this.base,
                this.canModify
        ) ?: return
        displayPullRequest(pullRequest, listOf())
    }
}