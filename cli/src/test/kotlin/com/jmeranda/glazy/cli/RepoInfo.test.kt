package com.jmeranda.glazy.cli

import com.jmeranda.glazy.lib.exception.NotInRepo
import org.junit.Test

import java.nio.file.Paths

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertNotNull

private val resourceDir: String = "${Paths.get(".").toRealPath()}/src/test/resources"

/**
 * Unit tests for for the getRepoDir top level function.
 */
class GetRepoDirTest {
    @Test fun testNoGit() {
        assertFailsWith<NotInRepo> { getRepoDir("/home/") }
    }

    @Test fun testGit() {
        val gitDirPath = getRepoDir()
        assertEquals("${Paths.get("..").toAbsolutePath().normalize()}", gitDirPath)
    }

    @Test fun testFromRoot() {
        assertFailsWith<NotInRepo> { getRepoDir("/") }
    }
}

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

    @Test fun testInRepo() {
        val (user: String?, name: String?) = getRepoName()
        assertNotNull(user)
        assertNotNull(name)
    }

    @Test fun testNoRepo() {
        assertFailsWith<NotInRepo> { getRepoName("/home/") }
    }

    @Test fun testFromRoot() {
        assertFailsWith<NotInRepo> { getRepoDir("/") }
    }
}