package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull

import com.jmeranda.glazy.lib.handler.ResponseCache

class ResponseCacheTest {
    private val cache = ResponseCache()

    @Test
    fun testGetCachedAccessToken() {
        assertEquals("bar", this.cache.token("foo"))
    }

    @Test
    fun testGetCachedAccessTokenBAD() {
        assertNull(this.cache.token("I_DO_NOT_EXIST"))
    }
}