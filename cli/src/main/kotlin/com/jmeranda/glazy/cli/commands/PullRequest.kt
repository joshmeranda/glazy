package com.jmeranda.glazy.cli.commands

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

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
 * Display information about the given [pullRequest],  with optional additional
 * [fields].
 */
fun displayPullRequest(pullRequest: PullRequest, fields: List<String>?) {
    var details = "[${pullRequest.number}] ${pullRequest.title}\n" +
            "draft : ${pullRequest.draft}\n" +
            "head: ${pullRequest.head}\n" +
            "base: ${pullRequest.head}\n" +
            "created: ${pullRequest.createdAt}"

    val badFields = mutableListOf<String>()

    // Concatenate additional fields to the details string.
    for (field in fields ?: listOf()) {
        // If property exists in class add to details, if not add to badFields.
        try {
            // Get repo property via input fields.
            val property = pullRequest::class
                    .memberProperties
                    .first { it.name == field }
                    as? KProperty1<PullRequest, Any>
            // Print the field name and value to the console.
            if (property != null) details += "\n$field: ${property.get(pullRequest)}"
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
            description=["The number of the desired pull request."],
            paramLabel="N")
    private val number: Int? = null

    @Option(names = ["-f", "--fields"],
            description = ["The fields to also show"],
            split = ",",
            paramLabel = "FIELD")
    private var fields: List<String>? = null

    override fun run() {
        // Retrieve pull request list
        val pullList: List<PullRequest?>? = if (this.number == null) {
            this.service?.getAllPullRequests()
        } else {
            listOf(this.service?.getPullRequest(this.number))
        }

        for (pull: PullRequest? in pullList ?: listOf()) {
            displayPullRequest(pull ?: continue, fields)
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
            description = ["The branch to merge."],
            paramLabel = "BRANCH",
            required = true)
    private lateinit var head: String

    @Option(names = ["--base"],
            description = ["The branch to merge the head branch into."],
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
        displayPullRequest(pullRequest ?: return, listOf())
    }

    companion object {
        /**
         * Class to provide the ability to create a pull request from an issue,
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

@Command(name = "patch",
        description = ["Send a patch to a pull request."],
        mixinStandardHelpOptions = true)
class PullUpdate: Runnable, PullCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the desired pull request."],
            paramLabel="N",
            required = true)
    private var number: Int? = null

    @Option(names = ["-t", "--title"],
            description = ["The new title for the path request."],
            paramLabel = "TITLE")
    private var title: String? = null

    @Option(names = ["-b", "--body"],
            description = ["The new body for the pull request."],
            paramLabel = "BODY")
    private var body: String? = null

    @Option(names = ["-s", "--state"],
            description = ["THe new state for the pull request."],
            paramLabel = "STATE")
    private var state: String? = null

    @Option(names = ["--base"],
            description = ["The new branch to merge into."],
            paramLabel = "BRANCH")
    private var base: String? = null

    @Option(names = ["-m", "--can-modify"],
            description = ["Specify that the maintainer can modify the request."])
    private var canModify: Boolean? = null

    override fun run() {
        val pullRequest = this.service?.patchPullRequest(this.number ?: return, this.title, this.body,
                this.state, this.base, this.canModify)
        displayPullRequest(pullRequest ?: return, listOf())
    }
}