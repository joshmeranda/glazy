package com.jmeranda.glazy.lib.handler.label

import com.fasterxml.jackson.module.kotlin.readValue
import com.jmeranda.glazy.lib.handler.GlazyLabelUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Label
import khttp.patch
import khttp.responses.Response

class LabelPatchHandler (
        header: GlazySimpleHeader,
        url: GlazyLabelUrl
) : Handler(header, url) {
    override fun handleRequest(): Label? {
        var body: String? = null

        // Deserialize request instance.
        try {
            body = mapper.writeValueAsString(this.request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val response: Response = patch(this.requestUrl,
                data = body,
                headers = this.getHeaders())

        if (! handleCode(response)) return null

        var label: Label? = null

        // Serialize received jason.
        try {
            label = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return label
    }
}