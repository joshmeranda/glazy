package com.jmeranda.glazy.cli

import org.junit.Test
import java.nio.file.Paths

import kotlin.test.assertEquals

class MainTest {
    private val resourceDir: String = "${Paths.get(".").toAbsolutePath()}/src/test/resources/"

    @Test
    fun testGetRepoNameSSH() {
        System.setProperty("user.dir", resourceDir + "ssh" )
        val (user: String?, name: String?) = getRepoName()
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }

    @Test
    fun testGetRepoNameHTTPS() {
        System.setProperty("user.dir", resourceDir + "https")

        val (user: String?, name: String?) = getRepoName()
        assertEquals("joshmeranda", user)
        assertEquals("glazy", name)
    }
}