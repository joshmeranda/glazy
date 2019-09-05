package com.jmeranda.glazy.lib

import org.junit.Test

import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNull

import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

import com.jmeranda.glazy.lib.handler.Handler

/**
 * Concrete handler class to allow for testing abstract class Handler.
 */
private class ConcreteHandler(token: String?): Handler<Int>(token) {
    override fun handleRequest(): Any?  = null

    override fun getRequestUrl(): String = "TEST_DOES_NOT_NEED_URL"

    fun getHeaders() = this.getAuthorizationHeaders()
}

/**
 * Data class for Handler class readers and writers.
 */
@JsonInclude(Include.NON_NULL)
private data class TestData(
        val firstName: String,
        val lastName: String? = null
)

class HandlerTest {
    private val nullHandler = ConcreteHandler(null)
    private val nonNullHandler = ConcreteHandler("bar")

    private val dataJson = """{"first_name":"foo","last_name":"bar"}"""
    private val dataClass = TestData(firstName ="foo", lastName = "bar")

    private val dataJsonIgnoreNull = """{"first_name":"foo"}"""
    private val dataClassIgnoreNull = TestData(firstName = "foo")

    @Test
    fun testAuthorizationHeaders() {
        assertEquals(
                mapOf("Authorization" to "token bar"), this.nonNullHandler.getHeaders()
        )
    }

    @Test
    fun testNullAuthorizationHeaders() {
        assertTrue(this.nullHandler.getHeaders().isEmpty())
    }

    @Test
    fun testHandlerReader() {
        val reader = Handler.mapper
        val data: TestData = reader.readValue(this.dataJson)

        assertEquals("foo", data.firstName)
        assertEquals("bar", data.lastName)
    }

    @Test
    fun testHandlerWriter() {
        val writer = Handler.mapper
        val data = writer.writeValueAsString(this.dataClass)

        assertEquals(this.dataJson, data)
    }

    @Test
    fun testHandlerReaderIgnoreNull() {
        val reader = Handler.mapper
        val data: TestData = reader.readValue(this.dataJsonIgnoreNull)

        assertEquals("foo", data.firstName)
        assertNull(data.lastName)
    }

    @Test
    fun testHandlerWriterIgnoreNull() {
        val writer = Handler.mapper
        val data = writer.writeValueAsString(this.dataClassIgnoreNull)

        assertEquals(this.dataJsonIgnoreNull, data)
    }
}