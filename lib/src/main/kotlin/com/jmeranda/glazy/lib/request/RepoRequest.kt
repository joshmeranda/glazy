package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
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
 * Request class for POST create requests.
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

/**
 * Request class for POST transfer requests.
 */
data class RepoTransferRequest(
        @JsonProperty("new_owner")
        val newOwner: String,

        @JsonProperty("team_ids")
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

        @JsonProperty("has_issues")
        val hasIssues: Boolean? = null,

        @JsonProperty("has_projects")
        val hasProjects: Boolean? = null,

        @JsonProperty("has_wiki")
        val hasWiki: Boolean? = null,

        @JsonProperty("is_template")
        val isTemplate: Boolean? = null,

        @JsonProperty("default_branch")
        val defaultBranch: String? = null,

        @JsonProperty("allow_squash_merge")
        val allowSquashMerge: Boolean? = null,

        @JsonProperty("allow_merge_commit")
        val allowMergeCommit: Boolean? = null,

        @JsonProperty("allow_rebase_merge")
        val allowRebaseMerge: Boolean? = null,

        @JsonProperty("archived")
        val archived: Boolean? = null
)

/**
 * Request class for DELETE requests
 */
data class RepoDeleteRequest(
        val name: String,

        val owner: String
)