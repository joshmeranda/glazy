package com.jmeranda.glazy.lib

/**
 * Represents the available endpoints for the github api v3.
 *
 * todo document properties with '@property tag'
 */
data class RootEndpoints (
        val currentUserUrl: String,
        val currentUserAuthorizationsHtmlUrl: String,
        val authorizationsUrl: String,
        val codeSearchUrl: String,
        val commitSearchUrl: String,
        val emailsUrl: String,
        val emojisUrl: String,
        val eventsUrl: String,
        val feedsUrl: String,
        val followersUrl: String,
        val followingUrl: String,
        val gistsUrl: String,
        val hubUrl: String,
        val issueSearchUrl: String,
        val issuesUrl: String,
        val keysUrl: String,
        val notificationsUrl: String,
        val organizationRepositoriesUrl: String,
        val organizationUrl: String,
        val publicGistsUrl: String,
        val rateLimitUrl: String,
        val repositoryUrl: String,
        val currentUserRepositoriesUrl: String,
        val starredUrl: String,
        val starredGistsUrl: String,
        val teamUrl: String,
        val userUrl: String,
        val userOrganizationsUrl: String,
        val userSearchUrl: String
)

const val ROOT_ENDPOINT: String = "https://api.github.com/"
