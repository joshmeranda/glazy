package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.jmeranda.glazy.lib.handler.Request

interface LabelRequest : Request {
    override val user: String
    override val name: String
    val label: String
}

data class LabelAllGetRequest (
        override val user: String,
        override val name: String
): Request

@JsonInclude(Include.NON_NULL)
data class LabelPostRequest (
        @JsonIgnore override val user: String,
        @JsonIgnore override val name: String,
        override val label: String,
        val color: String,
        val description: String? = null
) : LabelRequest

data class LabelDeleteRequest (
        override val user: String,
        override val name: String,
        override val label: String
) : LabelRequest