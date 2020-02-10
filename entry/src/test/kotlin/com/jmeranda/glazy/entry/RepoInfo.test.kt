package com.jmeranda.glazy.entry

import org.junit.Test

import java.nio.file.Paths

import kotlin.test.assertEquals
import kotlin.test.assertNull

private val resourceDir: String = "${Paths.get(".").toRealPath()}/src/test/resources"

/**
 * Unit tests for the getRepoInfo top level function.
 */
class GetRepoNameTest {
    @Test fun testSSH() {
        val (user: String?, name: String?) = getRepoName(targetPath = "$resourceDir/ssh")
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test fun testHTTPS() {
        val (user: String?, name: String?) = getRepoName(targetPath = "$resourceDir/https")
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test fun testNoUrl() {
        val (user: String?, name: String?) = getRepoName(targetPath = "$resourceDir/nourl")
        assertNull(user)
        assertNull(name)
    }

    @Test fun testBadUrl() {
        val (user: String?, name: String?) = getRepoName(targetPath = "$resourceDir/badurl")
        assertNull(user)
        assertNull(name)
    }
}