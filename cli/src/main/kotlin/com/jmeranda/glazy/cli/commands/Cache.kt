package com.jmeranda.glazy.cli.commands

import com.jmeranda.glazy.lib.service.CacheService
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "cache",
        description  = ["Perform operations on the glazy cache."],
        mixinStandardHelpOptions = true)
class CacheParent {
}

@Command(name = "clear",
        description = ["Clear the cache."],
        mixinStandardHelpOptions = true)
class ClearCache: Runnable {
    @Option(names = ["-t", "--token"],
            description = ["Do not ignore the access tokens when clearing cache."])
    private var ignoreToken = true

    override fun run() {
        CacheService.clear(this.ignoreToken)
    }
}

@Command(name = "refresh",
        description = ["Refresh the cache."],
        mixinStandardHelpOptions = true)
class RefreshCache: Runnable {
    @Option(names = ["-u", "--user"],
            description = ["THe user whose repositories to refresh."],
            required = true,
            paramLabel = "LOGIN")
    private var user: String = String()

    @Option(names = ["-n", "--name"],
            description = ["The name of the repository to refresh"],
            paramLabel = "NAME")
    private var name: String? = null

    override fun run() {
        val token = CacheService.token(this.user)

        if (name == null) {
            CacheService.refresh(token)
        } else {
            CacheService.refresh(this.user, this.name ?: return, token)
        }
    }
}

@Command(name = "token",
        description = ["Add token to the cache."],
        mixinStandardHelpOptions = true)
class TokenCache: Runnable {
    @Option(names = ["-t", "--token"],
            description = ["The token to be cached."],
            required = true,
            paramLabel = "TOKEN")
    private var token = String()

    @Option(names = ["-u", "--user"],
            description = ["The users login to associate with the token"],
            required = true,
            paramLabel = "LOGIN")
    private var user = String()

    override fun run() {
        CacheService.write(this.user, this.token)
    }
}