package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.request.LabelAllGetRequest
import com.jmeranda.glazy.lib.request.LabelDeleteRequest
import com.jmeranda.glazy.lib.request.LabelPatchRequest
import com.jmeranda.glazy.lib.request.LabelPostRequest

/**
 * Service providing access to label operation.
 *
 * @see [Service]
 */
class LabelService(user: String, name: String, token: String?): Service(user, name, token) {
    /**
     * Retrieve a list of all repository labels.
     *
     * @return A list of all repository labels, or null if the repository could not be found.
     */
    fun getAllLabels(): List<Label>? {
        val request = LabelAllGetRequest(this.user, this.name)
        val header = GlazySimpleHeader(this.token)
        val handler = GetHandler(header, labelRootUrl(request), request, Label::class)

        return handler.handleListRequest()
            ?.map { obj -> obj as Label }
    }

    /**
     * Create a new label in the repository.
     *
     * @param label The label title.
     * @param color The color of the label.
     * @param description The description body of the label.
     * @return The newly created label, or null if the label could not be created.
     */
    fun createLabel(
            label: String,
            color: String,
            description: String? = null
    ): Label? {
        val request = LabelPostRequest(this.user, this.name, label, color, description)
        val header = GlazySimpleHeader(this.token)
        val handler = PostHandler(header, labelRootUrl(request), request, Label::class)

        return handler.handleRequest() as Label?
    }

    /**
     * Delete  repository label.
     *
     * @param label The title of the target label.
     */
    fun deleteLabel(label: String) {
        val request = LabelDeleteRequest(this.user, this.name, label)
        val header = GlazySimpleHeader(this.token)
        DeleteHandler(header, labelUrl(request), request, Label::class).handleNoRequest()
    }

    /**
     * Edit an exisitng label.
     *
     * @param label The current title of the labaell.
     * @param newLabel The new title of the label.
     * @param color The new color for the label.
     * @param description The new describing body of the label.
     * @return The edited label, or null if the label could not be found or edited.
     */
    fun patchLabel(
            label: String,
            newLabel: String? = null,
            color: String? = null,
            description: String? = null
    ): Label? {
        val request = LabelPatchRequest(this.user, this.name, label, newLabel, color, description)
        val header = GlazySimpleHeader(this.token)
        val handler = PatchHandler(header, labelUrl(request), request, Label::class)

        return handler.handleRequest() as Label?
    }
}