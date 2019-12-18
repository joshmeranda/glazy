package com.jmeranda.glazy.lib.objects

/**
 * Describes a license (MIT, GPL, etc).
 */
data class License(
    val key: String,
    val name: String,
    val nodeId: String,
    val spdxId: String,
    val url: String? = null
)