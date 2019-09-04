package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertNull

import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.handler.ResponseCache
import com.jmeranda.glazy.lib.service.RepoService

class RepoServiceTest {
    private val service = RepoService(ResponseCache.token("foo"))

    @Test
    fun testGetRepoBad() {
        var repo: Repo? = null
        try {
            repo = this.service.getRepo("I_DO_NOT_EXIST", "foo")
        } catch (e: NoSuchRepo) {
            assertNull(repo)
        }
    }
}