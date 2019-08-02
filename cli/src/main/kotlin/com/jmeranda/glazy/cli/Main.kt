package com.jmeranda.glazy.cli

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.CACHE_DIR
import com.jmeranda.glazy.lib.service.RepoService
import com.jmeranda.glazy.cli.commands.*
import picocli.CommandLine

/**
 * Get the cached private access token of the user.
 */
fun getCachedAccessToken(user: String): String? {
    val tokenFile = "$CACHE_DIR/access_tokens"
    if (! Files.exists(Paths.get(tokenFile))) {
        return null
    }

    val contents: List<String> = File(tokenFile).readLines()

    for (line: String in contents) {
        val (username, token) = line.split(" ")

        if ( username == user) { return token }
    }

    return null
}

fun main(args: Array<String>) {
    var (user: String?, name: String?) = getRepoName()
    if (user == null || name == null) { exitProcess(1) }

    val accessToken = getCachedAccessToken(user)

    val service = RepoService(accessToken)
    val repo: Repo = service.getRepo(name, user) ?: throw NoSuchRepo(name)

    println("${repo.name}, ${repo.owner.login}")

    val cmd = CommandLine(Issue(repo, accessToken))
    cmd.execute(*args)
}