package com.jmeranda.gitkot.lib

/**
 * Describes a label to an issue
 */
data class Label (
        val id: Int,
        val nodeId: String,
        val url: String,
        val name: String,
        val color: String,
        val default: Boolean
)

/**
 * Describes a repo issue
 */
data class Issue (
        val url: String,
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
        val labels: Set<Label>,
        val state: String,
        val locked: Boolean,
        val assignee: Owner?,
        val assignees: Set<Owner>?,
        val mileStone: String?,
        val comments: Int,
        val createdAt: String,
        val updatedAtt: String?,
        val closedAt: String?,
        val authorAssociation: String,
        val body: String,
        val closedBy: String?
)