package com.jmeranda.glazy.lib.exception

class NoSuchRepo(
        private val repo: String,
        override val message: String = "No such repository '$repo'"
) : Exception(message)