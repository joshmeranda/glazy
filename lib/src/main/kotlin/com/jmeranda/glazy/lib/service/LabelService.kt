package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.GlazyIssueUrl
import com.jmeranda.glazy.lib.handler.GlazyLabelUrl
import com.jmeranda.glazy.lib.handler.GlazySimpleHeader
import com.jmeranda.glazy.lib.handler.GlazySimpleLabelUrl
import com.jmeranda.glazy.lib.handler.label.LabelAllGetHandler
import com.jmeranda.glazy.lib.handler.label.LabelDeleteHandler
import com.jmeranda.glazy.lib.handler.label.LabelPatchHandler
import com.jmeranda.glazy.lib.handler.label.LabelPostHandler
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.request.LabelAllGetRequest
import com.jmeranda.glazy.lib.request.LabelDeleteRequest
import com.jmeranda.glazy.lib.request.LabelPatchRequest
import com.jmeranda.glazy.lib.request.LabelPostRequest

class LabelService(
        private val user: String,
        private val name: String,
        private val token: String?
) {
    fun getAllLabels(): List<Label>? {
        val request = LabelAllGetRequest(this.user, this.name)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleLabelUrl(request)

        val handler = LabelAllGetHandler(header, url)

        return handler.handleRequest()
    }

    fun createLabel(
            label: String,
            color: String,
            description: String? = null
    ): Label? {
        val request = LabelPostRequest(this.user, this.name, label, color, description)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleLabelUrl(request)

        val handler = LabelPostHandler(header, url)

        return handler.handleRequest()
    }

    fun deleteLabel(label: String) {
        val request = LabelDeleteRequest(this.user, this.name, label)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyLabelUrl(request)

        LabelDeleteHandler(header, url).handleRequest()
    }

    fun patchLabel(
            label: String,
            newLabel: String? = null,
            color: String? = null,
            description: String? = null
    ): Label? {
        val request = LabelPatchRequest(this.user, this.name, label, newLabel, color, description)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyLabelUrl(request)

        val handler = LabelPatchHandler(header, url)

        return handler.handleRequest()
    }
}