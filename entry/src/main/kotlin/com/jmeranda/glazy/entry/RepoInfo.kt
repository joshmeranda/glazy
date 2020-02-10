package com.jmeranda.glazy.entry

import org.eclipse.jgit.lib.Config
import java.io.File

/**
 * Determine the user login and name of the current repository.
 *
 * If no targetPath is given, getRepoName will attempt to parse the 'config' file located in the current repositories
 * '.git' directory. The
 *
 * @param targetPath The path of the file to parse the information from, will most often will most often be
 *      <some_path>/.git/config.
 * @return A [Pair] specifying the repository owner and name, or a [Pair] of nulls if an error occurs.
 */
fun getRepoName(targetPath: String = "./.git/config"): Pair<String?, String?> {
    val cfg = Config()
    cfg.fromText(File(targetPath).readText())

    val fullNamePattern = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)
    val url = cfg.getString("remote", "origin", "url") ?: return Pair(null, null)
    val fullName = fullNamePattern.find(url)?.value?.split("/") ?: return Pair(null, null)

    return Pair(
        fullName[fullName.lastIndex - 1],
        fullName[fullName.lastIndex]
            .replace(".git", "")
    )
}