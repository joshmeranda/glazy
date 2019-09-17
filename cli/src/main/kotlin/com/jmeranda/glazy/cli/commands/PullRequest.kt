package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option

import com.jmeranda.glazy.lib.service.PullRequestService
import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.service.CacheService

fun displayPullRequest(pull: PullRequest) {
    println(pull.base)
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
class PullParent

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