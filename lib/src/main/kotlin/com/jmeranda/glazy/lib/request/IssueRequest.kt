package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

import com.jmeranda.glazy.lib.handler.Request

interface IssueRequest : Request {
    val number: Int
}

/**
 * Request class for GET requests for a specific issue.
 */
data class IssueGetRequest (
        override val user: String,
        override val name: String,
        override val number: Int
) : IssueRequest

/**
 * Request class for GET requests for all issues in a repository.
 */
data class IssueGetAllRequest (
       override val user: String,
       override val name: String
) : Request

/**
 * Request class for POST requests to create an issue.
 */
@JsonInclude(Include.NON_NULL)
data class IssuePostRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        val title: String,
        val body: String? = null,
        val milestone: Int? = null,
        val labels: List<String>? = null,
        val assignees: List<String>? = null
) : Request

/**
 * Request class for PATCH requests to edit an issue.
 */
@JsonInclude(Include.NON_NULL)
data class IssuePatchRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        @JsonIgnore override val number: Int,
        val title: String? = null,
        val body: String? = null,
        val state: String? = null,
        val milestone: Int? = null,
        val labels: List<String>? = null,
        val assignees: List<String>? = null
) : IssueRequest