package com.jmeranda.glazy.lib.handler.repo

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.Handler

import com.jmeranda.glazy.lib.objects.Repo

/**
 * Handle a request for all repositories associated with an authenticated
 * user. The [token] must be valid or an error code will be received.
 */
class RepoAllGetHandler(token: String): Handler(token) {
    private val repoUrl = endpoints.currentUserRepositoriesUrl

    override fun handleRequest(): List<Repo>? {
        val response =  get(this.getRequestUrl(), headers = this.getAuthorizationHeaders())
        var allRepos: List<Repo>? = null

        if (! handleCode(response)) return null

        // Serialize the received json.
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