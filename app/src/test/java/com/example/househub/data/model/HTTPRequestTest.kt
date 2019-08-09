package com.example.househub.data.model

import com.example.househub.HTTPRequest
import org.junit.Test

class HTTPRequestTest {
    @Test
    fun testHTTPRequest1() {
        val payload = mapOf("email" to "test@gmail.com", "pass" to "asdf")
        val url = "http://www.jtsalisbury.tech/sites/househub/api/user/login.php"

        fun success(res: String) {

        }
        fun fail(res: String) {

        }

        //val h = HTTPRequest(url, payload, success, fail)
        //h.open()

        assert(true)
    }
}