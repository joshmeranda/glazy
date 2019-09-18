package com.jmeranda.glazy.lib.objects

/**
 * Describes an issue milestone.
 */
data class Milestone(
        val url: String,
        val htmlUrl: String,
        val labelsUrl: String,
        val id: Int,
        val nodeId: String,
        val number: Int,
        val title: String,
        val description: String,
        val creator: Owner,
        val openIssues: Int,
        val closedIssues: Int,
        val state: String,
        val createdAt: String,
        val updatedAt: String? = null,
        val dueOn: String? = null,
        val closedAt: String? = null
)