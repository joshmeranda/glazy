package com.jmeranda.glazy.cli

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Get the name of the topmost directory in the current repository.
 * @param dir The starting directory, defaults to '.'
 * @return The name of the bottom-most repository directory.
 */
fun getRepoDir(dir: String = "."): String? {
    var cwd: Path = Paths.get(dir).toAbsolutePath().normalize()

    /* Walk up the tree to find a directory containing the '.git' dir. */
    while (! Files.exists(cwd.resolve(".git"))) {
        cwd = cwd.parent

        if (cwd == cwd.root) { return null }
    }

    return cwd.toString()
}

/**
 * Get the name of the current repository name.
 * @param dir The starting directory, defaults to '.'
 * @return The name of the repository.
 */
fun getRepoName(dir: String = "."): Pair<String?, String?> {
    val repoRegex = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)
    val contents: List<String> = File("${getRepoDir(dir)}/.git/config").readLines()
    var repoResult: MatchResult? = null

    /* Search for git repo name */
    for (line: String in contents) {
        repoResult = repoRegex.find(line)

        if (repoResult != null) { break }
    }

    if (repoResult == null) { return Pair(null, null) }

    val resultData: List<String> = repoResult.value.split("/")

    return Pair(resultData[0], resultData[1].replace(".git", ""))
}