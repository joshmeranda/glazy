package com.jmeranda.glazy.lib.request

/**
 * Request class for GET requests.
 */
data class RepoGetRequest(
        val name: String,
        val owner: String
)