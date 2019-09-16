package com.jmeranda.glazy.lib.objects

/**
 * Wrapper for pull request links
 */
data class RefWrapper (val href: String)

/**
 * Describes classes with pull request links.
 */
data class PullLinks (
        val self: RefWrapper,
        val hmtl: RefWrapper,
        val issue: RefWrapper,
        val comments: RefWrapper,
        val reviewComments: RefWrapper,
        val reviewComment: RefWrapper,
        val commits: RefWrapper,
        val statuses: RefWrapper
)

/**
 * Describes a repository pull request.
 */
data class PullRequest (
        val url: String,
        val id: Int,
        val nodeId: String,
        val htmlUrl: String,
        val diffUrl: String,
        val patchUrl: String,
        val issueUrl: String,
        val commitsUrl: String,
        val reviewCommentUrl: String,
        val reviewCommentsUrl: String,
        val commentsUrl: String,
        val statusesUrl: String,
        val number: Int,
        val state: String,
        val locked: Boolean,
        val title: String,
        val user: Owner,
        val body: String? = null,
        val labels: List<Label>? = null,
        val milestone: Milestone? = null,
        val activeLockReason: String,
        val createAt: String,
        val updatedAt: String,
        val closedAt: String? = null,
        val mergedAt: String? = null,
        val mergedCommitSha: String? = null,
        val assignee: Owner? = null,
        val assignees: List<Owner>? = null,
        val requestReviewers: List<Owner>? = null,
        val requestedTeams: List<Team>? = null,
        val head: Head,
        val base: Head,
        val _links: PullLinks,
        val authorAssociation: String,
        val draft: Boolean,
        val merged: Boolean,
        val mergeable: Boolean,
        val rebaseable: Boolean,
        val mergeableState: String,
        val mergedBy: Owner? = null,
        val comments: Int,
        val reviewComments: Int,
        val maintainerCanModify: Boolean,
        val commits: Int,
        val additions: Int,
        val deletions: Int,
        val changedFiles: Int
)