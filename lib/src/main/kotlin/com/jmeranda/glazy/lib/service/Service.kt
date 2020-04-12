package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.objects.Repo

import java.io.File

import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.storage.file.FileBasedConfig
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.FS

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
private val config: FileBasedConfig by lazy {
    try {
        FileBasedConfig(File(FileRepositoryBuilder().findGitDir().gitDir, "config"), FS.detect())
    } catch (e: RepositoryNotFoundException) {
        throw NotInRepo(System.getProperty("user.dir"))
    }
}

/**
 * Determine the token to use for authentication.
 *
 * @return The personal access token.
 */
fun getToken(): String? {
    return config.getString("github", null, "token")
}

/**
 * Determine the user to use for authentication.
 *
 * @return The username for authentication.
 */
fun getUser(): String? {
    return config.getString("github", null, "user")
}

/**
 * Change local remotes and directory names to reflect a repository name change.
 *
 * @param patchedRepo A repository object representing the current state of the repository.
 */
fun changeLocalName(patchedRepo: Repo) {
    val remote = config.getString("remote", "origin", "url") ?: return

    // rename the root directory
    val dir = File(System.getProperty("user.dir"))
    dir.renameTo(File(dir.parent + File.separator + patchedRepo.name))

    // change the remote configuration to the new url
    config.setString("remote", "origin", "url",
        if (remote.contains("https")) {
            patchedRepo.cloneUrl
        } else {
            patchedRepo.sshUrl
        })

    // save the new configuration to persistent storage
    config.save()
}