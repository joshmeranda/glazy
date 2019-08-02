package com.jmeranda.glazy.lib.exception

/**
 * Exception for non-existent repository.
 */
class NoSuchRepo(
        private val repo: String,
        override val message: String = "No such repository '$repo'"
) : Exception(message)