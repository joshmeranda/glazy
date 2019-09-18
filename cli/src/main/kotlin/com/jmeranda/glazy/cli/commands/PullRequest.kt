package com.jmeranda.glazy.cli.commands

import picocli.CommandLine
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.PullRequestService
import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.service.CacheService

/**
 * Display a [pull] to the console.
 */
fun displayPullRequest(pull: PullRequest) {
    println("[${pull.number}] ${pull.title}")
}

/**
 * Class to provide a service to perform operations and a [token] for
 * authentication. All pull su-commands must inherit from this class.
 */
open class PullCommand {
    var service: PullRequestService? = null
    private var token: String? = null

    init {
        val (user, name) = getRepoName()
        if (user != null) token = CacheService.token(user)

        if (name != null && user != null) this.service = PullRequestService(
                user, name, token)
    }
}

/**
 * Parent for all pull sub-commands.
 */
@Command(name = "pull",
        description = ["Perform operations on pull requests"],
        mixinStandardHelpOptions = true)
class PullParent: Runnable {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * List pull request, will list pull requests according to the value of
 * [number], if null all pull requests are listed.
 */
@Command(name = "list",
        description = ["List pull requests"],
        mixinStandardHelpOptions = true)
class PullList: Runnable, PullCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the desired issue."],
            paramLabel="N")
    private val number: Int? = null

    override fun run() {
        // Retrieve pull request list
        val pullList: List<PullRequest?>? = if (this.number == null) {
            this.service?.getAllPullRequests()
        } else {
            listOf(this.service?.getPullRequest(this.number))
        }

        for (pull: PullRequest? in pullList ?: listOf()) {
            displayPullRequest(pull ?: continue)
        }
    }
}

@Command(name = "init",
        description = ["Create a merge request."],
        mixinStandardHelpOptions = true)
class PullInit: Runnable, PullCommand() {
    @ArgGroup(exclusive = true, multiplicity = "1")
    private lateinit var exclusive: PullGroup

    @Option(names = ["--head"],
            description = ["The branch to merge the base into."],
            paramLabel = "BRANCH",
            required = true)
    private lateinit var head: String

    @Option(names = ["--base"],
            description = ["The brranch to be merged."],
            paramLabel = "BRANCH",
            required = true)
    private lateinit var base: String

    @Option(names = ["-b", "--body"],
            description = ["The contents of the pull request."],
            paramLabel = "BODY")
    private var body: String? = null

    @Option(names = ["-m", "--can-modify"],
            description = ["Specifies that the repository maintainers can modify the pull request."],
            paramLabel = "BOOL")
    private var canModify: Boolean? = null

    @Option(names = ["-d", "--draft"],
            description = ["Specifies whether the pull request is a draft or not."],
            paramLabel = "BOOL")
    private var draft: Boolean? = null

    override fun run() {
        val pullRequest = this.service?.createPullRequest(this.exclusive.title, this.exclusive.issue,
                this.head, this.base, this.body, this.canModify, this.draft)
        displayPullRequest(pullRequest ?: return)
    }

    companion object {
        /**
         * Class to provide the ability to create a pull request form an issue,
         * or use a title.
         */
        class PullGroup {
            @Option(names = ["-t", "--title"],
                    description = ["The title for the pull request."],
                    paramLabel = "TITLE",
                    required = true)
            var title: String? = null

            @Option(names = ["-i", "--issue"],
                    description = ["The issue number to create the pull request from."],
                    paramLabel = "N",
                    required = true)
            var issue: Int? = null
        }
    }
}