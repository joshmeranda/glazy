package com.jmeranda.glazy.lib

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Describes the owner of a repo
 */
data class Owner (
        val login: String,
        val id: Int,
        @JsonProperty("node_id")
        val nodeId: String,
        @JsonProperty("avatart_url")
        val avatarUrl: String,
        @JsonProperty("gravatar_url")
        val gravatarId: String,
        val url: String,
        @JsonProperty("html_url")
        val htmlUrl: String,
        @JsonProperty("followers_url")
        val followersUrl: String,
        @JsonProperty("following_url")
        val followingUrl: String,
        @JsonProperty("gists_url")
        val gistsUrl: String,
        @JsonProperty("starred_url")
        val starredUrl: String,
        @JsonProperty("subscriptions_url")
        val subscriptionsUrl: String,
        @JsonProperty("organization_url")
        val organizationsUrl: String,
        @JsonProperty("repos_url")
        val reposUrl: String,
        @JsonProperty("events_url")
        val eventsUrl: String,
        @JsonProperty("received_events_url")
        val receivedEventsUrl: String,
        val type: String,
        @JsonProperty("site_admin")
        val siteAdmin: Boolean
)

/**
 * Describes a license (MIT, GPL, etc)
 */
data class License(
        val key: String,
        val name: String,
        @JsonProperty("spdx_id")
        val spdxId: String,
        val url: String,
        @JsonProperty("node_id")
        val nodeId: String
)

/**
 * Describes a github repository.
 */
data class Repo(
        val id: Int,
        @JsonProperty("node_id")
        val nodeId: String,
        val name: String,
        @JsonProperty("full_name")
        val fullName: String,
        val private: Boolean,
        val owner: Owner,
        @JsonProperty("html_url")
        val htmlUrl: String,
        val description: String,
        val fork: Boolean,
        val url: String,
        @JsonProperty("forks_url")
        val forksUrl: String,
        @JsonProperty("keys_url")
        val keysUrl: String,
        @JsonProperty("collaborators_url")
        val collaboratorsUrl: String,
        @JsonProperty("teasm_url")
        val teamsUrl: String,
        @JsonProperty("hooks_url")
        val hooksUrl: String,
        @JsonProperty("issue_events_url")
        val issueEventsUrl: String,
        @JsonProperty("events_url")
        val eventsUrl: String,
        @JsonProperty("assignees_url")
        val assigneesUrl: String,
        @JsonProperty("branches_url")
        val branchesUrl: String,
        @JsonProperty("tags_url")
        val tagsUrl: String,
        @JsonProperty("blobs_url")
        val blobsUrl: String,
        @JsonProperty("git_tags_url")
        val gitTagsUrl: String,
        @JsonProperty("git_regs_url")
        val gitRefsUrl: String,
        @JsonProperty("trees_url")
        val treesUrl: String,
        @JsonProperty("statuses_url")
        val statusesUrl: String,
        @JsonProperty("languages_url")
        val languagesUrl: String,
        @JsonProperty("stargazers_url")
        val stargazersUrl: String,
        @JsonProperty("contributors_url")
        val contributorsUrl: String,
        @JsonProperty("subscribers_url")
        val subscribersUrl: String,
        @JsonProperty(" subscription_url")
        val subscriptionUrl: String,
        @JsonProperty("commits_url")
        val commitsUrl: String,
        @JsonProperty("git_commit_url")
        val gitCommitsUrl: String,
        @JsonProperty("comments_url")
        val commentsUrl: String,
        @JsonProperty("issue_comments_url")
        val issueCommentUrl: String,
        @JsonProperty("contents_url")
        val contentsUrl: String,
        @JsonProperty("compare_url")
        val compareUrl: String,
        @JsonProperty("merges_url")
        val mergesUrl: String,
        @JsonProperty("archive_url")
        val archiveUrl: String,
        @JsonProperty("downloads_url")
        val downloadsUrl: String,
        @JsonProperty("issues_url")
        val issuesUrl: String,
        @JsonProperty("pulls_url")
        val pullsUrl: String,
        @JsonProperty("milestones_url")
        val milestonesUrl: String,
        @JsonProperty("notifications_url")
        val notificationsUrl: String,
        @JsonProperty("labels_url")
        val labelsUrl: String,
        @JsonProperty("realeases_url")
        val releasesUrl: String,
        @JsonProperty("deployment_url")
        val deploymentsUrl: String,
        @JsonProperty("created_at")
        val createdAt: String,
        @JsonProperty("updated-at")
        val updatedAt: String,
        @JsonProperty("pushed_at")
        val pushedAt: String,
        @JsonProperty("git_url")
        val gitUrl: String,
        @JsonProperty("ssh_rul")
        val sshUrl: String,
        @JsonProperty("clonse_url")
        val cloneUrl: String,
        @JsonProperty("svn_url")
        val svnUrl: String,
        val homepage: String? = null,
        val size: Int,
        @JsonProperty("stargazers_count")
        val stargazersCount: Int,
        @JsonProperty("watchers_count")
        val watchersCount: Int,
        val language: String,
        @JsonProperty("has_issues")
        val hasIssues: Boolean,
        @JsonProperty("has_projects")
        val hasProjects: Boolean,
        @JsonProperty("has_downlaods")
        val hasDownloads: Boolean,
        @JsonProperty("has_wiki")
        val hasWiki: Boolean,
        @JsonProperty("has_pages")
        val hasPages: Boolean,
        @JsonProperty("forks_count")
        val forksCount: Int,
        @JsonProperty("mirror_url")
        val mirrorUrl: String? = null,
        val archived: Boolean,
        val disabled: Boolean,
        @JsonProperty("open_issues_count")
        val openIssuesCount: Int,
        val license: License,
        val forks: Int,
        @JsonProperty("open_issues")
        val openIssues: Int,
        val watchers: Int,
        @JsonProperty("default_branch")
        val defaultBranch: String,
        @JsonProperty("network_count")
        val networkCount: Int,
        @JsonProperty("subscribers_count")
        val subscribersCount: Int
)