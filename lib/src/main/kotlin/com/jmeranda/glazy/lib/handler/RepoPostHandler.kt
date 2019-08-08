package com.jmeranda.glazy.lib.handler

import khttp.post

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoPostRequest

class RepoPostHandler(
        private val repoRequest: RepoPostRequest,
        token: String?
): Handler(token) {

    override fun handleRequest(): Any? {
        //TODO handleRequest not implemented
    }

    override fun getRequestUrl(): String {
        //TODO getRequestUrl not implemented
    }
}