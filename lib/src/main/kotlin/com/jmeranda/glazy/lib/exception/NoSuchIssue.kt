package com.jmeranda.glazy.lib.exception

/**
 * Exception for non-existent Issue.
 */
class NoSuchIssue(
        private val number: Int,
        override val message: String = "No such issue '$number'"
) : Exception(message)