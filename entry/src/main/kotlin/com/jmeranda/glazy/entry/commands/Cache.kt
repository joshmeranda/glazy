package com.jmeranda.glazy.entry.commands

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.getToken
import com.jmeranda.glazy.lib.service.RepoService
import com.jmeranda.glazy.lib.service.clear
import com.jmeranda.glazy.lib.service.refresh

/**
 * Parent for all cache sub-commands.
 */
@Command(name = "cache",
        description = ["Perform operations on the glazy cache."],
        mixinStandardHelpOptions = true)
class CacheParent: Runnable {
    @Spec lateinit var spec: CommandSpec

    /**
     * When run before all child classes, with end program if no sub-command is passed as an argument.
     *
     * @throws CommandLine.ParameterException When no sub-command is entered by terminal.
     */
    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * Allow the user to clear the cache.
 */
@Command(name = "clear",
        description = ["Clear the cache."],
        mixinStandardHelpOptions = true)
class ClearCache: Runnable {
    override fun run() {
        clear()
    }
}

/**
 * Refresh the repositories in cache storage.
 *
 * @property user The username whose repositories to refresh. If null all repositories are refreshed.
 * @property name The name of the repository to refresh. If null all repositories are refreshed.
 */
@Command(name = "refresh",
        description = ["Refresh the cache."],
        mixinStandardHelpOptions = true)
class RefreshCache: Runnable {
    @Option(names = ["-u", "--user"],
            description = ["The user whose repositories to refresh."],
            required = true)
    private lateinit var user: String

    @Option(names = ["-n", "--name"],
            description = ["The name of the repository to refresh"])
    private var name: String? = null

    override fun run() {
        // Retrieve the potentially needed access token.
        val token = getToken()

        // Refresh the cache.
        if (name == null) {
            refresh()
        } else {
            refresh(this.user, this.name ?: return, RepoService(token))
        }
    }
}