package com.jmeranda.glazy.lib.objects

data class IssuePullRequest (
        val url: String,
        val html_Url: String,
        val diffUrl: String,
        val patchUrl: String
)

/**
 * Describes a repository issue.
 */
data class Issue(
        override val id: Int,
        override val nodeId: String,
        val assignee: User? = null,
        val assignees: List<User?>,
        val authorAssociation: String,
        val body: String? = null,
        val closedAt: String? = null,
        val closedBy: User? = null,
        val comments: Int,
        val commentsUrl: String,
        val createdAt: String,
        val eventsUrl: String,
        val htmlUrl: String,
        val labels: List<Label>,
        val labelsUrl: String,
        val locked: Boolean,
        val milestone: Milestone? = null,
        val number: Int,
        val pullRequest: PullRequest? = null,
        val repositoryUrl: String,
        val state: String,
        val title: String,
        val updatedAt: String? = null,
        val url: String,
        val user: User
) : GitObject(id, nodeId)