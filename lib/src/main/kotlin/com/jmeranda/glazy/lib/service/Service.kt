package com.jmeranda.glazy.lib.service

import org.eclipse.jgit.storage.file.FileRepositoryBuilder

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
 * Determine te token to use for authentication.
 *
 * @return The personal access token.
 */
fun getToken(): String {
    val builder = FileRepositoryBuilder()

    builder.findGitDir()
    builder.workTree = builder.gitDir

    return builder.build().config.getString("github", null, "token")
}

fun getUser(): String {
    val builder = FileRepositoryBuilder()

    builder.findGitDir()
    builder.workTree = builder.gitDir

    return builder.build().config.getString("github", null, "user")
}