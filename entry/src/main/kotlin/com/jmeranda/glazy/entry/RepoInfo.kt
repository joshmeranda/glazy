package com.jmeranda.glazy.entry

import com.jmeranda.glazy.lib.exception.NotInRepo
import org.eclipse.jgit.lib.Config
import org.eclipse.jgit.lib.RepositoryBuilder
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Determine the user login and name of the current repository.
 *
 * If no targetPath is given, getRepoName will attempt to parse the 'config' file located in the current repositories
 * '.git' directory. The
 *
 * @param targetPath The path of the file to parse the information from, will most often will most often be
 *      <some_path>/.git/config.
 * @return A [Pair] specifying the repository owner and name, or a [Pair] of nulls if an error occurs.
 * @throws NotInRepo When the target path is null and there is no provided target path to parse.
 */
fun getRepoName(targetPath: String? = null): Pair<String?, String?> {
    val path: File = if (targetPath == null) {
        try {
            File(RepositoryBuilder().findGitDir().gitDir.toString() + "/config")
        } catch (e: FileNotFoundException) {
            throw NotInRepo(System.getenv("PWD"))
        } catch (e: NullPointerException) {
            throw NotInRepo(System.getenv("PWD"))
        }
    } else {
        if (Files.exists(Paths.get(targetPath))) {
            File(targetPath)
        } else {
            return Pair(null, null)
        }
    }

    val cfg = Config()
    cfg.fromText(
        path.readText()
    )

    val fullNamePattern = Regex( "[a-zA-Z0-9]+/[a-zA-Z0-9\\-_]+\\.git$", RegexOption.UNIX_LINES)
    val url = cfg.getString("remote", "origin", "url") ?: return Pair(null, null)
    val fullName = fullNamePattern.find(url)?.value?.split("/") ?: return Pair(null, null)

    return Pair(
        fullName[fullName.lastIndex - 1],
        fullName[fullName.lastIndex]
            .replace(".git", "")
    )
}