package com.jmeranda.glazy.lib.handler

import khttp.get

import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.request.IssueGetAllRequest

/**
 * Handle a GET request for every available issue in a repository
 * @property issueRequest The request object used by the handler.
 * @property token The personal access token of the user.
 */
class IssueAllGetHandler(
        private val issueRequest: IssueGetAllRequest,
        token: String?
): Handler(token) {
    private val issueUrl: String = this.issueRequest.repo.issuesUrl

    override fun handleRequest(): List<Issue>? {
        val issueAsJson = get(this.getRequestUrl(), headers=this.getAuthorizationHeaders())
                        .text
        var allIssues: List<Issue>?

        try {
            allIssues = Handler.mapper.readValue(issueAsJson)
        } catch (e: Exception) {
            allIssues = null
            e.printStackTrace()
        }

        return allIssues
    }

    override fun getRequestUrl(): String = this.issueUrl
            .replace("{owner}", this.issueRequest.repo.owner.login)
            .replace("{repo}", this.issueRequest.repo.name)
            .replace("{/number}","")
}