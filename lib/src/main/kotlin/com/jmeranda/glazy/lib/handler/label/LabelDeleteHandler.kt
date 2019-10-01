package com.jmeranda.glazy.lib.handler.label

import khttp.delete
import com.jmeranda.glazy.lib.handler.GlazyLabelUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.Handler

class LabelDeleteHandler (
        header: GlazySimpleHeader,
        url: GlazyLabelUrl
) : Handler(header, url) {
    override fun handleRequest() {
        val response = delete(this.requestUrl,
                headers = this.getHeaders())
        handleCode(response)
    }
}