package com.jmeranda.glazy.lib.request

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
data class RepoPostRequest(
        val name: String,
        val description: String?,
        val homepage: String?,
        val private: Boolean = false,
        val hasIssues: Boolean = true,
        val hasProject: Boolean = true,
        val hasWiki: Boolean = true,
        val isTemplate: Boolean = false,
        val teamId: Int?,
        val autoInit: Boolean = false,
        val gitignoreTemplate: String?,
        val licenseTemplate: String?,
        val allowSquashMerge: Boolean = true,
        val allowMergeCommit: Boolean = true,
        val allowRebaseMerge: Boolean = true
)