package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option

import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Main command entry point.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy() {
}