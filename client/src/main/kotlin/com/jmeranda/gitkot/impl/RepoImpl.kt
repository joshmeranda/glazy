package com.jmeranda.gitkot.impl

import com.jmeranda.gitkot.IRepo

class RepoImpl : IRepo {
    /* TODO if issues is null call from API */
    override var issues: Set<IssueImpl>? = null
}