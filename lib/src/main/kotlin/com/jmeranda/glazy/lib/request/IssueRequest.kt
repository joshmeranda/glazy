package com.jmeranda.glazy.lib.request

import com.jmeranda.glazy.lib.Repo

data class IssueRequest (
        val repo: Repo,
        val number: Int
)

data class IssueAllRequest (
    val repo: Repo
)