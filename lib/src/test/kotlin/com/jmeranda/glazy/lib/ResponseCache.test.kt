package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull

import com.jmeranda.glazy.lib.service.cache.*
import java.io.File

class ResponseCacheTest {
    init {
        setCacheLocation(File("src/test/resource").canonicalPath)
    }

    @Test fun testGetCachedAccessToken() {
        assertEquals("bar", token("foo"))
    }

    @Test fun testGetCachedAccessTokenBAD() {
        assertNull(token("I_DO_NOT_EXIST"))
    }

    @Test fun testGetCachedRepo() {
        assertNull(repo("foo", "bar"))
    }
}