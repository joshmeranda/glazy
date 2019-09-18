package com.jmeranda.glazy.lib.request

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