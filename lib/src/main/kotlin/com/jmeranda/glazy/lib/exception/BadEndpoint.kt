package com.jmeranda.glazy.lib.exception

/**
 * Exception for non-existent endpoint.
 */
class BadEndpoint(
        override val message: String = "Unexpected or bad response from endpoint"
) : Exception(message)