package com.jmeranda.glazy.cli

import java.io.File

import kotlin.system.exitProcess

import com.beust.klaxon.Klaxon

import picocli.CommandLine
import picocli.CommandLine.Command

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.CACHE_DIR
import com.jmeranda.glazy.lib.service.RepoService
import com.jmeranda.glazy.cli.commands.*

/**
 * Get the cached private access token of the user.
 * @param user The name of the user whose access token to parse from the file.
 * @return The access token associated with the specified user.
 */
fun getCachedAccessToken(user: String): String? {
    /**
     * Data class used for parsing cached access-token pairs
     * @param user The user login key for each token.
     * @param token The token mapped to each user.
     */
    data class UserTokenPair(
        val user: String,
        val token: String
    )

    val tokenFile = File("$CACHE_DIR/access_tokens.json")
    val tokenArray = Klaxon().parseArray<UserTokenPair>(tokenFile)
    var token: String? = null

    for (pair: UserTokenPair in tokenArray ?: listOf(UserTokenPair("", ""))) {
        if (pair.user == user) { token = pair.token }
    }

    return token
}

@Command(name="glazy", description=["A command line interface to the github api."], mixinStandardHelpOptions=true)
class Glazy(): Runnable {
    var token: String? = null
    lateinit var repo: Repo

    override fun run() {
        val (user: String?, name: String?) = getRepoName()
        if (user == null || name == null) { return }
        this.token = getCachedAccessToken(user)
        val service = RepoService(this.token)
        this.repo = service.getRepo(name, user) ?: throw NoSuchRepo(name)

        println("${this.repo.name}, ${this.repo.owner.login}")
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