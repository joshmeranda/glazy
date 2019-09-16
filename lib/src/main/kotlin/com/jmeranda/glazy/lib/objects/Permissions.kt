package com.jmeranda.glazy.lib.objects

/**
 * Describes the permissions on a repository.
 */
data class Permissions(
        val admin: Boolean,
        val push: Boolean,
        val pull: Boolean
)