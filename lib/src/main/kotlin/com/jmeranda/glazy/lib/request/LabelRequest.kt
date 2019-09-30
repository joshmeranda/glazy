package com.jmeranda.glazy.lib.request

import com.jmeranda.glazy.lib.handler.Request

data class LabelAllGetRequest (
        override val user: String,
        override val name: String
): Request

data class LabelGetRequest (
        override val user: String,
        override val name: String,
        val label: String
): Request