package com.jmeranda.glazy.entry

import com.jmeranda.glazy.lib.exception.NotInRepo
import org.eclipse.jgit.errors.RepositoryNotFoundException
import org.eclipse.jgit.lib.Config

import java.io.File

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileBasedConfig
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.FS

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
    val cfg: Config = if (targetPath == null) {
        FileRepositoryBuilder()
            .setMustExist(true)
            .findGitDir()
            .build()
            .config
    } else {
        Config().let {
            FileBasedConfig(File(targetPath), FS.detect())
        }
    }
    val url = cfg.getString("remote", "origin", "url") ?: return Pair(null, null)

    return url.split(":").last()
        .split("/").let {
            Pair(it[it.lastIndex - 1],
                it[it.lastIndex].replace(".git", ""))
        }
}