package com.jmeranda.glazy.lib.handler

interface GlazyHeader {
    val token: String?

    fun getHeaders(): Map<String, String>
}

open class GlazySimpleHeader(override val token: String?) : GlazyHeader {
    override fun getHeaders(): Map<String, String> =
            if (this.token == null) { mapOf() } else { mapOf("Authorization" to "token ${this.token}") }
}

class GlazyDraftableHeader(override val token: String?) : GlazySimpleHeader(token) {
    override fun getHeaders(): Map<String, String> = super.getHeaders()
            .plus("Accept" to "application/vnd.github.shadow-cat-preview+json")
}

class GlazyTransferableHeader(override val token: String?) : GlazySimpleHeader(token) {
    override fun getHeaders(): Map<String, String> = super.getHeaders()
            .plus("Accept" to "application/vnd.github.nightshade-preview+json")
}