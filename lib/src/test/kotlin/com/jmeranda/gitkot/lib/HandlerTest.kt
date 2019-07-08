package com.jmeranda.gitkot.lib

import org.junit.Test

import kotlin.test.fail
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

import com.jmeranda.gitkot.lib.handler.*
import com.jmeranda.gitkot.lib.request.RepoRequest
import com.jmeranda.gitkot.lib.request.IssueRequest

class HandlerTest {
    /**
     * Ensure that the proper endpoint url is generated.
     */
    @Test
    fun testRepoRequestUrl() {
        val repoHandler = RepoGetHandler(
                RepoRequest("REPO", "OWNER")
        )

        val requestUrl: String = repoHandler.getRequestUrl()

        assertEquals(
                "https://api.github.com/repos/OWNER/REPO",requestUrl
        )
    }
}