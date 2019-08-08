package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertNull

import com.jmeranda.glazy.lib.exception.NoSuchIssue
import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService

class RepoServicetest {
    private val service = RepoService(ResponseCache().token("foo"))

    @Test
    fun testGetRepoBad() {
        var repo: Repo? = null
        try {
            repo = this.service.getRepo("I_DO_NOT_EXIST", "foo")
        } catch (e: NoSuchIssue) {
            assertNull(repo)
        }
    }
}