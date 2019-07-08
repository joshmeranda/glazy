package com.jmeranda.gitkot.lib.exception

class BadRequest (
        override val message: String = "Badly formed request"
): Exception(message)