package com.jmeranda.glazy.lib.handler.fork

import com.fasterxml.jackson.module.kotlin.readValue
import khttp.post
import com.jmeranda.glazy.lib.handler.GlazyForkUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Repo
import java.lang.Exception

class ForkPostHandler(
        header: GlazySimpleHeader,
        url: GlazyForkUrl
) : Handler(header, url) {
    override fun handleRequest(): Repo? {
        var body: String? = null

        // Deserialize the request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response = post(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (! handleCode(response)) return null

        var repo: Repo? = null

        // Serialize the received json.
        try {
            repo = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return repo
    }
}