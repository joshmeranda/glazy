package com.jmeranda.gitkot.lib.request

import com.jmeranda.gitkot.lib.Endpoints
import com.jmeranda.gitkot.lib.getEnpoints

abstract class Request {
    private companion object {
        val endpoints: Endpoints? = getEnpoints()
    }
}