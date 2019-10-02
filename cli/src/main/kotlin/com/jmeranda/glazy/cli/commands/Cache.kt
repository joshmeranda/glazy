package com.jmeranda.glazy.cli.commands

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.RepoService

/**
 * Parent for all cache sub-commands.
 */
@Command(name = "cache",
        description  = ["Perform operations on the glazy cache."],
        mixinStandardHelpOptions = true)
class CacheParent: Runnable {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * Allow the user to clear the cache, ignores the token file according
 * to [ignoreToken]. By default the tokens are ignored.
 */
@Command(name = "clear",
        description = ["Clear the cache."],
        mixinStandardHelpOptions = true)
class ClearCache: Runnable {
    @Option(names = ["-t", "--token"],
            description = ["Do not ignore the access tokens when clearing cache."])
    private var ignoreToken = true

    override fun run() {
        // Clear the cache.
        CacheService.clear(this.ignoreToken)
    }
}

/**
 * Refresh the cache, a [user] login must be specified. If no repository
 * [name] is passed, then all repositories under the [user] are refreshed.
 */
@Command(name = "refresh",
        description = ["Refresh the cache."],
        mixinStandardHelpOptions = true)
class RefreshCache: Runnable {
    @Option(names = ["-u", "--user"],
            description = ["THe user whose repositories to refresh."],
            required = true,
            paramLabel = "LOGIN")
    private lateinit var user: String

    @Option(names = ["-n", "--name"],
            description = ["The name of the repository to refresh"],
            paramLabel = "NAME")
    private var name: String? = null

    override fun run() {
        // Retrieve the potentially needed access token.
        val token = CacheService.token(this.user)

        // Refresh the cache.
        if (name == null) {
            CacheService.refresh(token)
        } else {
            CacheService.refresh(this.user, this.name ?: return, RepoService(token))
        }
    }
}

/**
 * Add a personal access [token] to the cache associated with the given
 * [user].
 */
@Command(name = "token",
        description = ["Add token to the cache."],
        mixinStandardHelpOptions = true)
class TokenCache: Runnable {
    @Option(names = ["-t", "--token"],
            description = ["The token to be cached."],
            required = true,
            paramLabel = "TOKEN")
    private lateinit var token: String

    @Option(names = ["-u", "--user"],
            description = ["The users login to associate with the token"],
            required = true,
            paramLabel = "LOGIN")
    private lateinit var user: String

    override fun run() {
        // Write the token to the cache.
        CacheService.write(this.user, this.token)
    }
}