package com.jmeranda.gitkot.lib.handler

import com.jmeranda.gitkot.lib.Endpoints
import com.jmeranda.gitkot.lib.getEndpoints
import com.jmeranda.gitkot.lib.request.Request

/**
 * A http request handler.
 *
 * Send http request to github api, and deserialize the Json response.
 *
 * @property request Request instance describing the request to be made.
 */
interface Handler {
    companion object {
        val endpoints: Endpoints? = getEndpoints()
    }

    val request: Request

    fun handleRequest(): Any?
}