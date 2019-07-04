package com.jmeranda.gitkot.lib.request

import com.jmeranda.gitkot.lib.Repo

data class IssueRequest (
        val repo: Repo,
        val number: Int
        ): Request
