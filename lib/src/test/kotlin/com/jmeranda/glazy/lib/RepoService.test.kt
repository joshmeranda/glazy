package com.jmeranda.glazy.lib

import com.jmeranda.glazy.lib.objects.Repo
import org.junit.Test
import kotlin.test.assertNull
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.RepoService
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.fail

class RepoServiceTest {
    private val service = RepoService(null)

    @Test fun testGetRepoBad() {
        assertNull(this.service.getRepo("foo", "I_DO_NOT_EXIST"))
    }

    @Test fun testGetRepo() {
        var repo = try {
            assertNotNull(this.service.getRepo("joshmeranda", "glazy"))
        } catch (e: NoSuchRepo) {
            fail("Could not find existing repository 'joshmeranda/glazy'.")
        }
    }
}