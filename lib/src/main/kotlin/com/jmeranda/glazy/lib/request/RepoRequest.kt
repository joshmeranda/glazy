package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

/**
 * Request class for GET requests.
 */
data class RepoGetRequest(
        val name: String,
        val owner: String
)

/**
 * Request class for all issues.
 */
data class RepoAllGetRequest(
        val user: String
)

/**
 * Request class for POST create requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoPostRequest(
        val name: String,
        val description: String? = null,
        val homepage: String? = null,
        val private: Boolean? = null,
        val hasIssues: Boolean? = null,
        val hasProject: Boolean? = null,
        val hasWiki: Boolean? = null,
        val isTemplate: Boolean? = null,
        val teamId: Int? = null,
        val autoInit: Boolean? = null,
        val gitignoreTemplate: String? = null,
        val licenseTemplate: String? = null,
        val allowSquashMerge: Boolean? = null,
        val allowMergeCommit: Boolean? = null,
        val allowRebaseMerge: Boolean? = null
)

/**
 * Request class for POST transfer requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoTransferRequest(
        @JsonIgnore val user: String,
        @JsonIgnore val name: String,
        val newOwner: String,
        val teamIds: List<Int>? = null
)

/**
 * Request class for PATCH requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoPatchRequest(
        @JsonIgnore val owner: String,
        @JsonIgnore val currentName: String,
        val name: String? = null,
        val description: String? = null,
        val homepage: String? = null,
        val private: Boolean? = null,
        val hasIssues: Boolean? = null,
        val hasProjects: Boolean? = null,
        val hasWiki: Boolean? = null,
        val isTemplate: Boolean? = null,
        val defaultBranch: String? = null,
        val allowSquashMerge: Boolean? = null,
        val allowMergeCommit: Boolean? = null,
        val allowRebaseMerge: Boolean? = null,
        val archived: Boolean? = null
)

/**
 * Request class for DELETE requests
 */
data class RepoDeleteRequest(
        val name: String,
        val owner: String
)