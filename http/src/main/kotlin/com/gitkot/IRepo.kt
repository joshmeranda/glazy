package com.gitkot

import com.gitkot.impl.IssueImpl

interface IRepo {
    var issues: Set<IssueImpl>?
}