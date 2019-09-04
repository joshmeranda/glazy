package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull

import com.jmeranda.glazy.lib.handler.ResponseCache

class ResponseCacheTest {
    @Test
    fun testGetCachedAccessToken() {
        assertEquals("bar", ResponseCache.token("foo"))
    }

    @Test
    fun testGetCachedAccessTokenBAD() {
        assertNull(ResponseCache.token("I_DO_NOT_EXIST"))
    }
}