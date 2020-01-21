package com.jmeranda.glazy.entry

import com.jmeranda.glazy.lib.exception.NotInRepo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Get the name of the repository of the given [path].
 *
 * @param path The starting path from which to determine the repository.
 * @return The [java.nio.file.Path] to the repo directory.
 * @throws NotInRepo When [path] is not inside a git repository.
 */
fun getRepoDir(path: String = "."): Path {
    var cwd: Path = Paths.get(path).toAbsolutePath().normalize()

    /* Walk up the tree to find a directory containing the '.git' dir. */
    while (! Files.exists(cwd.resolve(".git"))) {
        if (cwd == cwd.root) throw NotInRepo(path)

        cwd = cwd.parent
    }

    return cwd
}

/**
 * Determine the user login and name of the current repository.
 *
 * If no targetPath is given, getRepoName will attempt to parse the 'config' file located in the current repositories
 * '.git' fir.
 *
 * @param targetPath The path of the file to parse the information from, will most often will most often be
 *      <some_path>/.git/config.
 * @return A [Pair] specifying the repository owner and name.
 */
fun getRepoName(targetPath: String? = null): Pair<String?, String?> {
    val repoRegex = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)
    val contents: List<String>

    // Get the contents of the git config file or the target file
    try {
        contents = if (targetPath  == null) {
            File("${getRepoDir()}/.git/config").readLines()
        } else {
            File(targetPath).readLines()
        }
    } catch (e: NotInRepo) {
        return Pair(null, null)
    }

    var repoResult: MatchResult? = null

    // Search for git repo name
    for (line: String in contents) {
        repoResult = repoRegex.find(line)

        if (repoResult != null) { break }
    }

    if (repoResult == null) return Pair(null, null)

    val resultData: List<String> = repoResult.value.split("/")

    return Pair(resultData[0], resultData[1].replace(".git", ""))
}