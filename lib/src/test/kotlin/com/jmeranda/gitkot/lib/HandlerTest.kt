package com.jmeranda.gitkot.lib

import org.junit.Test

import khttp.get
import khttp.responses.Response

import kotlin.test.fail
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

import com.jmeranda.gitkot.lib.handler.*
import com.jmeranda.gitkot.lib.request.RepoRequest
import com.jmeranda.gitkot.lib.request.IssueRequest

class HandlerTest {

    @Test
    fun testRepoRequestUrl() {
        val repoHandler: RepoGetHandler = RepoGetHandler(
                RepoRequest("REPO", "OWNER")
        )

        val requestUrl: String = repoHandler.getRequestUrl()

        assertEquals(
                requestUrl,
                "https://api.github.com/repos/OWNER/REPO",
                "Failure: bad requestUrl $requestUrl"
        )
    }
}