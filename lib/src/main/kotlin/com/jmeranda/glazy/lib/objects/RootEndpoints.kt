package com.jmeranda.glazy.lib.objects

/**
 * The starting endpoint which must be used to determine all other api
 * endpoints.
 */
const val ROOT_ENDPOINT: String = "https://api.github.com/"

/**
 * Represents the available endpoints for the github api v3.
 */
data class RootEndpoints(
    val authorizationsUrl: String,
    val codeSearchUrl: String,
    val commitSearchUrl: String,
    val currentUserAuthorizationsHtmlUrl: String,
    val currentUserRepositoriesUrl: String,
    val currentUserUrl: String,
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
    val labelSearchUrl: String,
    val notificationsUrl: String,
    val organizationRepositoriesUrl: String,
    val organizationUrl: String,
    val publicGistsUrl: String,
    val rateLimitUrl: String,
    val repositorySearchUrl: String,
    val repositoryUrl: String,
    val starredGistsUrl: String,
    val starredUrl: String,
    val organizationTeamsUrl: String,
    val userOrganizationsUrl: String,
    val userRepositoriesUrl: String,
    val userSearchUrl: String,
    val userUrl: String
)
