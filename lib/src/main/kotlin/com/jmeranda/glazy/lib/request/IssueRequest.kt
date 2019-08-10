package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
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
        @JsonIgnore val repo: Repo,
        val title: String,
        val body: String? = null,
        val milestone: Int? = null,
        val labels: List<String>? = listOf(),
        val assignees: List<String>? = listOf()
)

/**
 * Request class for PATCH requests to edit an issue.
 */
@JsonInclude(Include.NON_NULL)
data class IssuePatchRequest (
        @JsonIgnore val repo: Repo,
        @JsonIgnore val number: Int,
        val title: String? = null,
        val body: String? = null,
        val state: String? = null,
        val milestone: Int? = null,
        val labels: List<String>? = null,
        val assignees: List<String>? = null
)