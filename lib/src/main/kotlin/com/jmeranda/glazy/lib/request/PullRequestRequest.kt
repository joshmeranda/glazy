package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

/**
 * Request class for GET requests for all repository pull requests.
 */
data class PullGetAllRequest (
        val owner: String,
        val name: String
)

/**
 * Request class for GET requests for a specific repository pull request.
 */
data class PullGetRequest (
        val owner: String,
        val name: String,
        val number: Int
)

/**
 * Request class for POST requests to create a pull request.
 */
@JsonInclude(Include.NON_NULL)
data class PullPostRequest (
        val owner: String,
        val name: String,
        val title: String? = null,
        val issue: Int? = null,
        val head: String,
        val base: String,
        val body: String? = null,
        val canModify: Boolean? = null,
        val draft: Boolean? = null
)