package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
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
        val handler = GetHandler(header, url, Label::class)

        return handler.handleListRequest() as List<Label>?
    }

    fun createLabel(
            label: String,
            color: String,
            description: String? = null
    ): Label? {
        val request = LabelPostRequest(this.user, this.name, label, color, description)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleLabelUrl(request)
        val handler = PostPatchHandler(header, url, Label::class)

        return handler.handleRequest() as Label?
    }

    fun deleteLabel(label: String) {
        val request = LabelDeleteRequest(this.user, this.name, label)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyLabelUrl(request)
        DeleteHandler(header, url, Label::class).handleNoRequest()
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
        val handler = PostPatchHandler(header, url, Label::class)

        return handler.handleRequest() as Label?
    }
}