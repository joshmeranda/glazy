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
class GetRepoDirTest() {
    @Test fun testNoGit() {
        assertFailsWith<NotInRepo> { getRepoDir("/home/") }
    }

    @Test fun testGit() {
        val gitDirPath = getRepoDir("$resourceDir/ssh/.git")
        assertNotNull(gitDirPath)
        assertEquals("$resourceDir/ssh", gitDirPath)
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
        val (user: String?, name: String?) = getRepoName()
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test fun testHTTPS() {
        val (user: String?, name: String?) = getRepoName("$resourceDir/https/.git")
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test fun testNoRepo() {
        val (user: String?, name: String?) = getRepoName("/home/")
        assertNull(user)
        assertNull(name)
    }

    @Test fun testFromRoot() {
        val(user: String?, name: String?) = getRepoName("/")
        assertNull(user)
        assertNull(name)
    }
}