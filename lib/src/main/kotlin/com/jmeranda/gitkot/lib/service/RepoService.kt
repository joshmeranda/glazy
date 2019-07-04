package com.jmeranda.gitkot.lib.service

import com.jmeranda.gitkot.lib.Repo
import com.jmeranda.gitkot.lib.request.RepoRequest

/* todo make no abstract */
abstract class RepoService(user: String, repository: String) {
    abstract fun getRepo(repoRequest: RepoRequest): Repo
}