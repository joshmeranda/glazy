package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueGetAllRequest

/**
 * Handle a GET request for every available issue in a repository
 *
 * @property request The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssueAllGetHandler(
        private val request: IssueGetAllRequest,
        token: String?
): Handler<Issue>(token) {
    private val issueUrl: String = this.request.repo.issuesUrl

    override fun handleRequest(): List<Issue>? {
        val response = get(this.getRequestUrl(), headers=this.getAuthorizationHeaders())
        var allIssues: List<Issue>? = null

        handleCode(response.statusCode)

        try {
            allIssues = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allIssues
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.request.repo.owner.login)
            .replace("{repo}", this.request.repo.name)
            .replace("{/number}","")
}