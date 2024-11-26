package io.github.yinjinlong.spring.boot.test

import java.net.HttpURLConnection
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author YJL
 */
class TestRespJson {

    fun getResp(path: String): String {
        val url = URL("http://localhost:8765/$path?arg=a")
        val httpCoon = url.openConnection() as HttpURLConnection
        httpCoon.requestMethod = "GET"
        return httpCoon.inputStream.readAllBytes().decodeToString()
    }

    @Test
    fun test() {
        val v = getResp("test/void")
        assertEquals("{\"code\":0}", v)
    }

    @Test
    fun testEmpty() {
        val v = getResp("test/empty")
        assertEquals("", v)
    }

}