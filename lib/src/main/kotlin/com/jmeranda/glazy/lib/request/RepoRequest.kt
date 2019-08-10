package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

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
        val name: String,
        val description: String? = null,
        val homepage: String? = null,
        val private: Boolean? = null,
        @JsonProperty("has_issues")
        val hasIssues: Boolean? = null,
        @JsonProperty("has_project")
        val hasProject: Boolean? = null,
        @JsonProperty("has_wiki")
        val hasWiki: Boolean? = null,
        @JsonProperty("is_template")
        val isTemplate: Boolean? = null,
        @JsonProperty("team_id")
        val teamId: Int? = null,
        @JsonProperty("auto_init")
        val autoInit: Boolean? = null,
        @JsonProperty("gitignore_template")
        val gitignoreTemplate: String? = null,
        @JsonProperty("license_template")
        val licenseTemplate: String? = null,
        @JsonProperty("allow_squash_merge")
        val allowSquashMerge: Boolean? = null,
        @JsonProperty("allowMergeCommit")
        val allowMergeCommit: Boolean? = null,
        @JsonProperty("allow_rebase_merge")
        val allowRebaseMerge: Boolean? = null
)