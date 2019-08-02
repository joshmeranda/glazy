package com.jmeranda.glazy.lib.request

import com.beust.klaxon.Json
import com.jmeranda.glazy.lib.Repo

/**
 * Request class for GET requests for a specific issue.
 */
data class IssueGetRequest (
        val repo: Repo,
        val number: Int
)

/**
 * Request class for GET requests for all issues in a repository.
 */
data class IssueGetAllRequest (
       val repo: Repo
)

/**
 * Request class for POST requests to create an issue.
 */
data class IssuePostRequest (
        @Json(ignored = true)
        val repo: Repo,
        val title: String,
        val body: String?,
        val milestone: Int?,
        val labels: List<String>?,
        val assignees: List<String>?
)

/**
 * Request class for PATCH requests to edit an issue.
 */
data class IssuePatchRequest (
        @Json(ignored = true)
        val repo: Repo,
        @Json(ignored = true)
        val number: Int,
        val title: String?,
        val body: String?,
        val state: String?,
        val milestone: Int?,
        val labels: List<String>?,
        val assignees: List<String>?
)