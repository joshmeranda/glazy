package com.jmeranda.glazy.lib.objects

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Wrapper for pull request links
 */
data class RefWrapper (val href: String)

/**
 * Describes classes with pull request links.
 */
data class LinkRelations (
        val self: RefWrapper,
        val html: RefWrapper,
        val issue: RefWrapper,
        val comments: RefWrapper,
        val reviewComments: RefWrapper,
        val reviewComment: RefWrapper,
        val commits: RefWrapper,
        val statuses: RefWrapper
)

/**
 * Describes the head or base of the pull request.
 */
data class Head(
        val label: String,
        val ref: String,
        val repo: Repo,
        val sha: String,
        val user: User
)

data class PullRequest(
        override val id: Int,
        override val nodeId: String,
        val additions: Int,
        val activeLockReason: String? = null,
        val assignee: User? = null,
        val assignees: List<Any>,
        val authorAssociation: String,
        val base: Head,
        val body: String? = null,
        val changedFiles: Int,
        val closedAt: String? = null,
        val comments: Int,
        val commentsUrl: String,
        val commits: Int,
        val commitsUrl: String,
        val createdAt: String,
        val deletions: Int,
        val diffUrl: String,
        val draft: Boolean, // preview field
        val head: Head,
        val htmlUrl: String,
        val issueUrl: String,
        val labels: List<Label>? = null,
        @JsonProperty("_links")
        val links: LinkRelations,
        val locked: Boolean,
        val maintainerCanModify: Boolean,
        val mergeCommitSha: String,
        val mergeable: Boolean,
        val mergeableState: String,
        val merged: Boolean,
        val mergedAt: Any?,
        val mergedBy: Any?,
        val milestone: Milestone? = null,
        val number: Int,
        val patchUrl: String,
        val rebaseable: Boolean,
        val requestedReviewers: List<User>,
        val requestedTeams: List<Team>,
        val reviewCommentUrl: String,
        val reviewComments: Int,
        val reviewCommentsUrl: String,
        val state: String,
        val statusesUrl: String,
        val title: String,
        val updatedAt: String? = null,
        val url: String,
        val user: User
) : GitObject(id, nodeId)
