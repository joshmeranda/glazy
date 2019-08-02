package com.jmeranda.glazy.lib.exception

/**
 * Exception for poorly formed request.
 */
class BadRequest (
        override val message: String = "Badly formed request"
): Exception(message)