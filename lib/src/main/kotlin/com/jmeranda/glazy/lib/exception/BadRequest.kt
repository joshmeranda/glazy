package com.jmeranda.glazy.lib.exception

class BadRequest (
        override val message: String = "Badly formed request"
): Exception(message)