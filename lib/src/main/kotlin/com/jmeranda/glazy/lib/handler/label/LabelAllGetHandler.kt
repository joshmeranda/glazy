package com.jmeranda.glazy.lib.handler.label

import com.fasterxml.jackson.module.kotlin.readValue
import khttp.get
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazySimpleLabelUrl
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Label

class LabelAllGetHandler(
        header: GlazySimpleHeader,
        url: GlazySimpleLabelUrl
): Handler(header, url) {
    override fun handleRequest(): List<Label>? {
        val response = get(this.requestUrl, headers = this.getHeaders())
        var allLabels: List<Label>? = null

        if (! Handler.handleCode(response)) return null

        // Serialize the received json
        try {
            allLabels = mapper.readValue(response.text)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allLabels
    }
}