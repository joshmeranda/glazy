package com.jmeranda.gitkot.client.impl

import com.jmeranda.gitkot.client.IRepo

class RepoImpl : IRepo {
    /* TODO if issues is null call from API */
    override var issues: Set<IssueImpl>? = null
}