package com.jmeranda.gitkot.client

import com.jmeranda.gitkot.client.impl.IssueImpl

interface IRepo {
    var issues: Set<IssueImpl>?
}