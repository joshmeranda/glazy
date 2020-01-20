package com.jmeranda.glazy.entry

import com.jmeranda.glazy.lib.exception.NotInRepo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Return the name of the topmost git repository directory, given the
 * starting [dir].
 */
fun getRepoDir(dir: String = "."): String {
    var cwd: Path = Paths.get(dir).toAbsolutePath().normalize()

    /* Walk up the tree to find a directory containing the '.git' dir. */
    while (! Files.exists(cwd.resolve(".git"))) {
        if (cwd == cwd.root) throw NotInRepo(dir)

        cwd = cwd.parent
    }

    return cwd.toString()
}

/**
 * Determine the user login and name of the current repository, given
 * the starting [dir]. If targetPath is specified, the dir argument if
 * supplied is ignored.
 */
fun getRepoName(dir: String = ".", targetPath: String? = null): Pair<String?, String?> {
    val repoRegex = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)

    /* Get the contents of the git config file or the target file */
    val contents: List<String> = if (targetPath  == null) {
        File("${getRepoDir(dir)}/.git/config").readLines()
    } else {
        File(targetPath).readLines()
    }

    var repoResult: MatchResult? = null

    /* Search for git repo name */
    for (line: String in contents) {
        repoResult = repoRegex.find(line)

        if (repoResult != null) { break }
    }

    if (repoResult == null) return Pair(null, null)

    val resultData: List<String> = repoResult.value.split("/")

    return Pair(resultData[0], resultData[1].replace(".git", ""))
}