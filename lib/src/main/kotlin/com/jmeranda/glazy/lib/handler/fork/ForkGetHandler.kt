package com.jmeranda.glazy.lib.handler.fork

import khttp.get
import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyForkUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Repo

class ForkGetHandler(
        header: GlazySimpleHeader,
        url: GlazyForkUrl
) : Handler(header, url) {
    override fun handleRequest(): List<Repo>? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        var allForks: List<Repo>? = null

        if (! handleCode(response)) return null

        // Serialize the received json.
        try {
            allForks = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allForks
    }
}