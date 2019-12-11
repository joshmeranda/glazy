package com.jmeranda.glazy.lib.objects

/**
 * Describes a label to an issue.
 */
data class Label(
        override val id: Int,
        override val nodeId: String,
        val color: String,
        val default: Boolean,
        val description: String,
        val name: String,
        val url: String
) : GitObject(id, nodeId)