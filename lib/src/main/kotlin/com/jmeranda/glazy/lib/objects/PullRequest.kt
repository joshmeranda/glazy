package com.jmeranda.glazy.lib.objects

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Wrapper for pull request links
 */
@JsonInclude(Include.NON_NULL)
data class RefWrapper (val href: String)

/**
 * Describes classes with pull request links.
 */
@JsonInclude(Include.NON_NULL)
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
 * Describes a repository pull request.
 */
@JsonInclude(Include.NON_NULL)
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
        val activeLockReason: String? = null,
        val createdAt: String,
        val updatedAt: String,
        val closedAt: String? = null,
        val mergedAt: String? = null,
        val mergeCommitSha: String? = null,
        val assignee: Owner? = null,
        val assignees: List<Owner>? = null,
        val requestedReviewers: List<Owner>? = null,
        val requestedTeams: List<Team>? = null,
        val head: Head,
        val base: Head,
        @JsonProperty("_links")
        val links: LinkRelations? = null,
        val authorAssociation: String,
        val draft: Boolean,
        val merged: Boolean,
        val mergeable: Boolean,
        val rebaseable: Boolean,
        val mergeableState: String? = null,
        val mergedBy: Owner? = null,
        val comments: Int,
        val reviewComments: Int,
        val maintainerCanModify: Boolean,
        val commits: Int,
        val additions: Int,
        val deletions: Int,
        val changedFiles: Int
)