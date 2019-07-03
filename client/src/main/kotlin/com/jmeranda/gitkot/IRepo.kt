package com.jmeranda.gitkot

import com.jmeranda.gitkot.impl.IssueImpl

interface IRepo {
    var issues: Set<IssueImpl>?
}