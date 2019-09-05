package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull

import com.jmeranda.glazy.lib.service.CacheService
import java.io.File

class ResponseCacheTest {
    init {
        CacheService.setCacheLocation(File("src/test/resource").canonicalPath)
    }

    @Test
    fun testGetCachedAccessToken() {
        assertEquals("bar", CacheService.token("foo"))
    }

    @Test
    fun testGetCachedAccessTokenBAD() {
        assertNull(CacheService.token("I_DO_NOT_EXIST"))
    }
}