package com.jmeranda.glazy.lib.objects

/**
 * Describes an issue milestone.
 */
data class Milestone(
    val closedAt: String? = null,
    val closedIssues: Int,
    val createdAt: String,
    val creator: User,
    val description: String,
    val dueOn: String? = null,
    val htmlUrl: String,
    val id: Int,
    val labelsUrl: String,
    val nodeId: String,
    val number: Int,
    val openIssues: Int,
    val state: String,
    val title: String,
    val updatedAt: String? = null,
    val url: String
)