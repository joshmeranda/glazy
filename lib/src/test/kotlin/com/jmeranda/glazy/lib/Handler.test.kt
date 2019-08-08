package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.handler.getKlaxonFieldRenamer
import com.jmeranda.glazy.lib.handler.getRootEndpoints
import com.jmeranda.glazy.lib.handler.ResponseCache

class HandlerTest {
    private val klaxonParser = getKlaxonFieldRenamer()

    @Test
    fun testFieldRenamer() {
        /**
         * Class to ensure that fields  are renamed from snake to camel
         * case when serializing JSON data.
         */
        data class TestData( val firstName: String, val lastName: String)
        val jsonData = """{"first_name": "foo", "last_name": "bar"}"""
        val dsl = klaxonParser.parse<TestData>(jsonData)

        assertNotNull(dsl)
        assertEquals("foo", dsl.firstName)
        assertEquals("bar", dsl.lastName)
    }

    @Test
    fun testRootEndpointsBad() {
        var rootEndpoints: RootEndpoints? = null
        try {
            rootEndpoints = getRootEndpoints("I_AM_A_BAD_ENDPOINT", klaxonParser,
                    ResponseCache())
        } catch (e: BadEndpoint) {
            assertNull(rootEndpoints)
        }
    }

    @Test
    fun testRootEndpointGood() {
        val rootEndpoints: RootEndpoints? = getRootEndpoints(ROOT_ENDPOINT, klaxonParser,
                ResponseCache())
        assertNotNull(rootEndpoints)
    }
}