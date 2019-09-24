package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.jmeranda.glazy.lib.handler.Request

interface PullRequest : Request {
    val number: Int
}

/**
 * Request class for GET requests for all repository pull requests.
 */
data class PullGetAllRequest (
        override val user: String,
        override val name: String
) : Request

/**
 * Request class for GET requests for a specific repository pull request.
 */
data class PullGetRequest (
        override val user: String,
        override val name: String,
        override val number: Int
) : PullRequest

/**
 * Request class for POST requests to create a pull request.
 */
@JsonInclude(Include.NON_NULL)
data class PullPostRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        val title: String? = null,
        val issue: Int? = null,
        val head: String,
        val base: String,
        val body: String? = null,
        val canModify: Boolean? = null,
        val draft: Boolean? = null
) : Request

/**
 * Request class or PUT requests to edit a pull request.
 */
@JsonInclude(Include.NON_NULL)
data class PullPatchRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        @JsonIgnore override val number: Int,
        val title: String? = null,
        val body: String? = null,
        val state: String? = null,
        val base: String? = null,
        val maintainerCanModify: Boolean? = null
) : PullRequest