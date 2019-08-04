package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.request.RepoGetRequest

class HandlerTest {
    /**
     * Ensure that the proper endpoint url is generated.
     */
    @Test
    fun testRepoRequestUrl() {
        val repoHandler = RepoGetHandler(
                RepoGetRequest("REPO", "OWNER")
        )

        val requestUrl: String = repoHandler.getRequestUrl()

        assertEquals(
                "https://api.github.com/repos/OWNER/REPO", requestUrl
        )
    }
}