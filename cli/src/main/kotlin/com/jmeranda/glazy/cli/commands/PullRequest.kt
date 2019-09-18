package com.jmeranda.glazy.cli.commands

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.PullRequestService
import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.service.CacheService

fun displayPullRequest(pull: PullRequest) {
    println("[${pull.number}] ${pull.title}")
}

open class PullCommand {
    var service: PullRequestService? = null
    private var token: String? = null

    init {
        val (user, name) = getRepoName()
        if (user != null) token = CacheService.token(user)

        if (name != null && user != null) this.service = PullRequestService(
                user,
                name,
                this.token)
    }
}

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