package com.jmeranda.glazy.lib.objects

/**
 * Describes a label to an issue.
 */
data class Label (
        val id: Int,
        val nodeId: String,
        val url: String,
        val name: String,
        val color: String,
        val default: Boolean
)