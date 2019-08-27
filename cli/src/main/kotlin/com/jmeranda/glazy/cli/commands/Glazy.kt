package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.ArgGroup

import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Main command entry point.s
 *
 * @property cache The cache Object from which to pull cached information.
 * @property token The personal access token for the user.
 * @property repoService The repo service for sub-commands to use.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy(): Runnable {
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

    private val cache = ResponseCache()
    var token: String? = null
    lateinit var repoService: RepoService

//    override fun run() {
//        if (user == null || name == null) {
//            val repoName = getRepoName()
//            this.user = repoName.first
//            this.name = repoName.second
//            if (this.user == null || this.name == null) { return }
//        }
//        val user = this.user ?: return
//
//        this.token = this.cache.token(user)
//        this.repoService = RepoService(this.token)
//    }

    override fun run() {
    }
}