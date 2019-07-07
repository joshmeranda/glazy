package com.jmeranda.gitkot.lib.exception

class BadEndpoint(
        override val message: String = "Unexpected or bad response from endpoint"
) : Exception(message)