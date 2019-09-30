package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazySimpleLabelUrl
import com.jmeranda.glazy.lib.handler.label.LabelAllGetHandler
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.request.LabelAllGetRequest

class LabelService(
        private val user: String,
        private val name: String,
        private val token: String?
) {
    fun getAllLabels(): List<Label> {
        val request = LabelAllGetRequest(this.user, this.name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleLabelUrl(request)

        val handler = LabelAllGetHandler(header, url)

        return handler.handleRequest() ?: throw Exception()
    }
}