package com.jmeranda.glazy.cli

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Get the name of the topmost directory in the current repository.
 * @return The name of the topmost repository directory.
 */
private fun getRepoDir(): String {
    var cwd: Path = Paths.get(".").toAbsolutePath().normalize()

    while (! Files.exists(cwd.resolve(".git"))) {
        cwd = cwd.parent
    }

    return cwd.toString()
}

/**
 * Get the name of the current repository name.
 * @return The name of the repository.
 */
fun getRepoName(): Pair<String?, String?> {
    val repoRegex = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)
    val contents: List<String> = File("${getRepoDir()}/.git/config").readLines()
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