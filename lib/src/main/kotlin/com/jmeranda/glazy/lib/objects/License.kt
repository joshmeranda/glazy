package com.jmeranda.glazy.lib.objects

/**
 * Describes a license (MIT, GPL, etc).
 */
data class License(
        val key: String,
        val name: String,
        val spdxId: String? = null,
        val url: String? = null,
        val nodeId: String? = null
)