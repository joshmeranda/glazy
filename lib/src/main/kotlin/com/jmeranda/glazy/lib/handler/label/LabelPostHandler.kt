package com.jmeranda.glazy.lib.handler.label

import com.fasterxml.jackson.module.kotlin.readValue
import khttp.post
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazySimpleLabelUrl
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Label
import khttp.responses.Response

class LabelPostHandler (
        header: GlazySimpleHeader,
        url: GlazySimpleLabelUrl
) : Handler(header, url) {
    override fun handleRequest(): Label? {
        var body: String? = null

        // Deserialize the request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch(e: Exception) {
            e.printStackTrace()
        }

        val response: Response = post(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (! handleCode(response)) return null

        var label: Label? = null

        // Serialize the received json.
        try {
            label = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return label
    }
}