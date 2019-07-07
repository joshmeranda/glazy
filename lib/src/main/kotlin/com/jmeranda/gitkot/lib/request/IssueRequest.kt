package com.jmeranda.gitkot.lib.request

data class IssueRequest (
        val name: String,
        val owner: String,
        val number: Int
)
