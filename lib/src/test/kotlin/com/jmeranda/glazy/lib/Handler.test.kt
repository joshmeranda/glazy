package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.handler.getRootEndpoints
import com.jmeranda.glazy.lib.handler.ResponseCache

class HandlerTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun testRootEndpointsBad() {
        var rootEndpoints: RootEndpoints? = null
        try {
            rootEndpoints = getRootEndpoints("I_AM_A_BAD_ENDPOINT", mapper,
                    ResponseCache())
        } catch (e: BadEndpoint) {
            assertNull(rootEndpoints)
        }
    }

    @Test
    fun testRootEndpointGood() {
        val rootEndpoints: RootEndpoints? = getRootEndpoints(ROOT_ENDPOINT, mapper,
                ResponseCache())
        assertNotNull(rootEndpoints)
    }
}