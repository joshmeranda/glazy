package com.jmeranda.glazy.lib.handler

/**
 * Basic interface to allow for simple request header creation.
 *
 * @property token Th personal access token to use for authentications.
 */
interface GlazyHeader {
    val token: String?
    val headers: Map<String, String>
}

/**
 * A simple header implementation providing basic token authentication.
 */
open class GlazySimpleHeader(override val token: String?) : GlazyHeader {
    override val headers: Map<String, String> by lazy {
            if (this.token == null) {
                mapOf()
            } else {
                mapOf("Authorization" to "token ${this.token}")
            }
    }
}

/**
 * Provides access to the pull request draftable beta.
 */
class GlazyDraftableHeader(override val token: String?) : GlazySimpleHeader(token) {
    override val headers: Map<String, String> by lazy {
        super.headers.plus("Accept" to "application/vnd.github.shadow-cat-preview+json")
    }
}

/**
 * Provides access to the repository transfer beta.
 */
class GlazyTransferableHeader(override val token: String?) : GlazySimpleHeader(token) {
    override val headers: Map<String, String> by lazy {
        super.headers.plus("Accept" to "application/vnd.github.nightshade-preview+json")
    }
}