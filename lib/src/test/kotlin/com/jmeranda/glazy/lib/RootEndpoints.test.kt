package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertNull
import kotlin.test.assertNotNull

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.handler.getRootEndpoints
import com.jmeranda.glazy.lib.handler.ResponseCache

class RootEndpointsTest {
    private val mapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    private val cache = ResponseCache()

    @Test
    fun testRootEndpointsBad() {
        var rootEndpoints: RootEndpoints? = null
        try {
            rootEndpoints = getRootEndpoints("I_AM_A_BAD_ENDPOINT", mapper, cache)
        } catch (e: BadEndpoint) {
            assertNull(rootEndpoints)
        }
    }

    @Test
    fun testRootEndpointGood() {
        val rootEndpoints: RootEndpoints? = getRootEndpoints(ROOT_ENDPOINT, mapper, cache)
        assertNotNull(rootEndpoints)
    }
}