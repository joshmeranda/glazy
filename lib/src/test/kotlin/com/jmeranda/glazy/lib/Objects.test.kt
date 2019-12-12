package com.jmeranda.glazy.lib

import com.jmeranda.glazy.lib.service.IssueService
import com.jmeranda.glazy.lib.service.LabelService
import com.jmeranda.glazy.lib.service.PullRequestService
import com.jmeranda.glazy.lib.service.RepoService
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Please noe that when running these tests, you will quickly deplete the
 * standard unauthenticated rate limit of 60 per minute for the api.
 */

const val user: String = "joshmeranda"
const val name: String = "glazy"
const val badName: String = "I_DO_NOT_EXIST"
val token = null

class RepoTest {
    private val service = RepoService(token)

    @Test fun testRepo() {
        assertNotNull(this.service.getRepo(user, name))
    }

    @Test fun testRepoBad() {
        assertNull(this.service.getRepo(badName, badName))
    }

    @Test fun testRepoListBad() {
        assertNull(this.service.getAllRepos())
    }
}

class LabelTest {
    private val service = LabelService(user, name, token)
    private val badService = LabelService(badName, badName, token)

    @Test fun testLabelList() {
        assertNotNull(service.getAllLabels())
    }

    @Test fun testLabelListBad() {
        assertNull(badService.getAllLabels())
    }
}

class PullRequestTest {
    private val service = PullRequestService(user, name, token)
    private val badService = PullRequestService(badName, badName, token)

    @Test fun testPullRequest() {
        assertNotNull(service.getPullRequest(10))
    }

    @Test fun testPullRequestBad() {
        assertNull(service.getPullRequest(-1))
    }

    @Test fun testPullRequestList() {
        assertNotNull(service.getAllPullRequests())
    }

    @Test fun testPullRequestListBad() {
        assertNull(badService.getAllPullRequests())
    }
}

class IssueTest {
    private val service = IssueService(user, name, token)
    private val badService = IssueService(badName, badName, token)

    @Test fun testIssue() {
        assertNotNull(service.getIssue(1))
    }

    @Test fun testIssueBad() {
        assertNull(service.getIssue(-1))
    }

    @Test fun testIssueList() {
        assertNotNull(service.getAllIssues())
    }

    @Test fun testIssueListBad() {
        assertNull(badService.getAllIssues())
    }
}