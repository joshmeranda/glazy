package com.jmeranda.glazy.cli

import java.io.File

import com.beust.klaxon.Klaxon

import picocli.CommandLine
import picocli.CommandLine.Command

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService
import com.jmeranda.glazy.cli.commands.*

/**
 * Main command entry point.
 * @property token The personal access token for the user.
 * @property repo The repo object from which to gather endpoint urls.
 */
@Command(name="glazy", description=["A command line interface to the github api."], mixinStandardHelpOptions=true)
class Glazy(): Runnable {
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

fun main(args: Array<String>) {
    CommandLine(Glazy())
            .addSubcommand(CommandLine(IssueParent())
                    .addSubcommand(IssueList())
                    .addSubcommand(IssueAdd())
                    .addSubcommand(IssuePatch()))
            .execute(*args)
}