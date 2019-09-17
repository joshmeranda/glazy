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