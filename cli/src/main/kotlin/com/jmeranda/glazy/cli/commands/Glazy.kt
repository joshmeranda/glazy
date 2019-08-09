package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option

import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Main command entry point.
 *
 * @property token The personal access token for the user.
 * @property repo The repo object from which to gather endpoint urls.
 */
@Command(name="glazy",
        description=["A command line interface to the github api."],
        mixinStandardHelpOptions=true)
class Glazy(): Runnable {
    @Option(names=["-u", "--user"],
            description = ["The user login for the desired repository."])
    private val cache = ResponseCache()
    var token: String? = null
    lateinit var repo: Repo

    override fun run() {
        val (user: String?, name: String?) = getRepoName()
        if (user == null || name == null) { return }
        this.token = this.cache.token(user)
        val service = RepoService(this.token)
        this.repo = service.getRepo(name, user)
    }
}