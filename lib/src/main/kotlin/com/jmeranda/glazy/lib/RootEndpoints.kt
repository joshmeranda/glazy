package com.jmeranda.glazy.lib

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the available endpoints for the github api v3.
 */
data class RootEndpoints (
        @JsonProperty("current_user_url")
        val currentUserUrl: String,
        @JsonProperty("current_user_authorizations_html_url")
        val currentUserAuthorizationsHtmlUrl: String,
        @JsonProperty("authorizations_url")
        val authorizationsUrl: String,
        @JsonProperty("code_search_url")
        val codeSearchUrl: String,
        @JsonProperty("commit_search_url")
        val commitSearchUrl: String,
        @JsonProperty("emails_url")
        val emailsUrl: String,
        @JsonProperty("emojis_url")
        val emojisUrl: String,
        @JsonProperty("events_url")
        val eventsUrl: String,
        @JsonProperty("feeds_url")
        val feedsUrl: String,
        @JsonProperty("followers_url")
        val followersUrl: String,
        @JsonProperty("following_url")
        val followingUrl: String,
        @JsonProperty("gists_url")
        val gistsUrl: String,
        @JsonProperty("hub_url")
        val hubUrl: String,
        @JsonProperty("issue_search_url")
        val issueSearchUrl: String,
        @JsonProperty("issues_url")
        val issuesUrl: String,
        @JsonProperty("keys_url")
        val keysUrl: String,
        @JsonProperty("notifications_url")
        val notificationsUrl: String,
        @JsonProperty("organization_repositories_url")
        val organizationRepositoriesUrl: String,
        @JsonProperty("organization_url")
        val organizationUrl: String,
        @JsonProperty("public_gists_url")
        val publicGistsUrl: String,
        @JsonProperty("rate_limit_url")
        val rateLimitUrl: String,
        @JsonProperty("repository_url")
        val repositoryUrl: String,
        @JsonProperty("repository_search_url")
        val repositorySearchUrl: String,
        @JsonProperty("current_user_repositories_url")
        val currentUserRepositoriesUrl: String,
        @JsonProperty("starred_url")
        val starredUrl: String,
        @JsonProperty("starred_gists_url")
        val starredGistsUrl: String,
        @JsonProperty("team_url")
        val teamUrl: String,
        @JsonProperty("user_url")
        val userUrl: String,
        @JsonProperty("user_organizations_url")
        val userOrganizationsUrl: String,
        @JsonProperty("user_repositories_url")
        val userRepositoriesUrl: String,
        @JsonProperty("user_search_url")
        val userSearchUrl: String
)

const val ROOT_ENDPOINT: String = "https://api.github.com/"
