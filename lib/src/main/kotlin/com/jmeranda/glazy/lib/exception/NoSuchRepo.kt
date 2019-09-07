package com.jmeranda.glazy.lib.exception

/**
 * Exception for non-existent repository.
 */
class NoSuchRepo(
        private val name: String,
        override val message: String = "No such repository '$name'"
) : Exception(message)