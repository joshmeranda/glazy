package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.makeVerbose
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Logger

/**
 * Services will provide high level access to operations on a repository and its elements.
 *
 * @param user The username of the repository.
 * @param name The name of the repository.
 * @param token The access token to be used for authentication.
 */
abstract class Service(
    protected val user: String,
    protected val name: String,
    protected val token: String?)

/**
 * A list of all config files for git.
 */
private val configList: List<File> by lazy {
    val list = mutableListOf<File>()

    if (Files.exists(Paths.get(System.getenv("PWD") + "/.git/config"))) {
        list.add(File(System.getenv("PWD") + "/.git/config"))
    }

    if (Files.exists(Paths.get(System.getenv("HOME") + "/.gitconfig"))) {
        list.add(File(System.getenv("HOME") + "/.gitconfig"))
    }

    if (Files.exists(Paths.get("/etc/gitconfig"))) {
        list.add(File("/etc/gitconfig"))
    }

    list.toList()
}

/**
 * Determine the token to use for authentication.
 *
 * @return The personal access token.
 */
fun getToken(): String? {
    val cfg = Config()

    for (path in configList) {
        cfg.fromText(path.readText())
        return cfg.getString("github", null, "token") ?: continue
    }

    return null
}

/**
 * Determine the user to use for authentication.
 *
 * @return The username for authentication.
 */
fun getUser(): String? {
    val cfg = Config()

    for (path in configList) {
        cfg.fromText(path.readText())
        return cfg.getString("github", null, "user") ?: continue
    }

    return null
}