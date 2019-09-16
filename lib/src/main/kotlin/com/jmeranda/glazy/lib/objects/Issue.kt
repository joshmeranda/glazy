package com.jmeranda.glazy.lib.objects

/**
 * Describes a repository issue.
 */
data class Issue (
        val url: String? = null,
        val repositoryUrl: String,
        val labelsUrl: String,
        val commentsUrl: String,
        val eventsUrl: String,
        val htmlUrl: String,
        val id: Int,
        val nodeId: String,
        val number: Int,
        val title: String,
        val user: Owner,
        val labels: List<Label>,
        val state: String,
        val locked: Boolean,
        val assignee: Owner? = null,
        val assignees: List<Owner>,
        val milestone: Milestone? = null,
        val comments: Int,
        val createdAt: String,
        val updatedAt: String? = null,
        val closedAt: String?,
        val authorAssociation: String,
        val body: String? = null,
        val closedBy: Owner? = null
)