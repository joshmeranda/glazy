package com.jmeranda.glazy.entry.commands

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.cache.*
import com.jmeranda.glazy.lib.service.RepoService

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
 *
 * @property ignoreToken Specify to leave the token 'as-is' while cleaning cache.
 */
@Command(name = "clear",
        description = ["Clear the cache."],
        mixinStandardHelpOptions = true)
class ClearCache: Runnable {
    @Option(names = ["-t", "--token"],
            description = ["Do not ignore the access tokens when clearing cache."])
    private var ignoreToken = true

    override fun run() {
        clear(this.ignoreToken)
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
        val token = token(this.user)

        // Refresh the cache.
        if (name == null) {
            refresh(token)
        } else {
            refresh(this.user, this.name ?: return, RepoService(token))
        }
    }
}

/**
 * Add a personal access token to the cache.
 *
 * @property user The username to associate with the access token.
 * @property token The value of the token.
 */
@Command(name = "token",
        description = ["Add token to the cache."],
        mixinStandardHelpOptions = true)
class TokenCache: Runnable {
    @Parameters(index = "0", description = ["The users login to associate with the token"])
    private lateinit var user: String

    @Parameters(index = "1", description = ["The token to be cached."])
    private lateinit var token: String

    override fun run() {
        write(this.user, this.token)
    }
}