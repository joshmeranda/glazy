package com.jmeranda.glazy.cli

import org.junit.Test

import java.nio.file.Paths

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

import com.jmeranda.glazy.cli.getRepoDir

class RepoInfoTest {
    private val resourceDir: String = "${Paths.get(".").toRealPath()}/src/test/resources"

    @Test
    fun testGetRepoDirNoGit() {
        assertNull(getRepoDir("/home/"))
    }

    @Test
    fun testGetRepoDirGit() {
        val gitDirPath = getRepoDir("$resourceDir/ssh/.git")
        assertNotNull(gitDirPath)
        println("=== $resourceDir/ssh ===")
        assertEquals("$resourceDir/ssh", gitDirPath)
    }

    @Test
    fun testGetRepoNameSSH() {
        val (user: String?, name: String?) = getRepoName()
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test
    fun testGetRepoNameHTTPS() {
        val (user: String?, name: String?) = getRepoName("$resourceDir/https/.git")
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }
}