package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

import com.jmeranda.glazy.lib.handler.Request

/**
 * Request class for GET requests.
 */
data class RepoGetRequest(
        override val user: String,
        override val name: String
) : Request

/**
 * Request class for POST create requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoPostRequest(
        override val user: String,
        override val name: String,
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
) : Request

/**
 * Request class for POST transfer requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoTransferRequest(
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        val newOwner: String,
        val teamIds: List<Int>? = null
) : Request

/**
 * Request class for PATCH requests.
 */
@JsonInclude(Include.NON_NULL)
data class RepoPatchRequest(
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        @JsonProperty("name") val newName: String? = null,
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
) : Request

/**
 * Request class for DELETE requests
 */
data class RepoDeleteRequest(
        override val user: String,
        override val name: String
) : Request

/**
 * Request class for forking a repository.
 */
data class RepoForkRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        val organization: String? = null
) : Request