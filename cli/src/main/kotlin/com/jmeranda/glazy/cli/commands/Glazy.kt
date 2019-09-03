package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option

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
    private val cache = ResponseCache()
    var token: String? = null
    lateinit var repoService: RepoService

    override fun run() {
    }
}