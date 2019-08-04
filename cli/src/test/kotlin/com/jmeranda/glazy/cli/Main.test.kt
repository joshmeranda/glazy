package com.jmeranda.glazy.cli

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull

class MainTest {
    @Test
    fun testGetCachedAccessToken() {
        assertEquals("bar", getCachedAccessToken("foo"))
    }

    @Test
    fun testGetCachedAccessTokenBAD() {
        assertNull(getCachedAccessToken("I_DO_NOT_EXIST"))
    }
}