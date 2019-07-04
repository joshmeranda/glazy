package com.jmeranda.gitkot.lib.request

class RepoRequest(user: String, repo: String): Request {
    val user: String = user
    val repo: String = repo
}