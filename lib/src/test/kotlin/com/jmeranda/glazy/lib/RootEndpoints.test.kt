package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertNotNull

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.jmeranda.glazy.lib.objects.ROOT_ENDPOINT
import com.jmeranda.glazy.lib.objects.RootEndpoints

import com.jmeranda.glazy.lib.exception.BadEndpoint
import com.jmeranda.glazy.lib.handler.getRootEndpoints
import kotlin.test.assertFailsWith

class RootEndpointsTest {
    private val mapper = jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    @Test fun testRootEndpointsBad() {
        assertFailsWith<BadEndpoint> { getRootEndpoints("I_AM_A_BAD_ENDPOINT", mapper) }
    }

    @Test fun testRootEndpointGood() {
        val rootEndpoints: RootEndpoints? = getRootEndpoints(ROOT_ENDPOINT, mapper)
        assertNotNull(rootEndpoints)
    }
}