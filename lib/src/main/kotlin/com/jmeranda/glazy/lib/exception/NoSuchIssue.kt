package com.jmeranda.glazy.lib.exception

class NoSuchIssue(
        private val number: Int,
        override val message: String = "No such issue '$number'"
) : Exception(message)