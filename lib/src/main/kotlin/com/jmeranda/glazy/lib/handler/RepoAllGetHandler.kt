package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoAllGetRequest

class RepoAllGetHandler(private val request: RepoAllGetRequest,
                        token: String?
): Handler<List<Repo>>(token) {
    private val repoUrl = endpoints.currentUserRepositoriesUrl

    override fun handleRequest(): List<Repo>? {
        val response =  get(this.getRequestUrl(), headers = this.getAuthorizationHeaders())
        var allRepos: List<Repo>? = null

        handleCode(response.statusCode)

        try {
            allRepos = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allRepos
    }

    override fun getRequestUrl(): String = this.repoUrl
            .replace(Regex("\\{.*}"), "")
}