package com.jmeranda.glazy.lib.request

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
 * Request class for POST requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoPostRequest(
        val name: String? = null,
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