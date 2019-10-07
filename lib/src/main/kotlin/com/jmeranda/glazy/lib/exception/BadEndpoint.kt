package com.jmeranda.glazy.lib.exception

/**
 * Exception for non-existent endpoint.
 */
class BadEndpoint(
        private val endpointUrl: String
) : Exception("Unexpected or bad response from endpoint at '$endpointUrl'")