package com.example.househub.data.model

import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Test

class LoginTest {
    @Test
    fun testEncodeDecode() {
        val email = "test@gmail.com"
        val password = "asdf"

        val jwt: JWT = JWT()
        val gson = Gson()
        val realToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.xc6B91TEdgoaYm7ZARLvGKVYWXWW4FG0Qk4ivGp2ZeY3lUMGK+r+65+EgyggDFOv.MDY1OThjN2FjZjA0NzE3OWFjODRlZDQ4MjExZWI0ZWVlNTRmOTk3NDIwODc1MjA2YjUyNDc0NjVjNjUwZGZkMQ=="
        val payload = mapOf("email" to email, "pass" to password)
        val token = jwt.generateToken(payload)

        val token2 = mapOf("token" to token)
        var tokenEnc = gson.toJson(token2)

        val url = "http://u747950311.hostingerapp.com/househub/api/user/login.php"
        var success = ""
        var fail = ""
        val h = HTTPRequest(url, payload, success, fail)
        val results = h.open()
        success = results.first
        fail = results.second

        if(fail != "") {
            // login failed
        }

        if(!jwt.verifyToken(success)) {
            // login failed
        }

        val test = jwt.decodePayload(success)
        val blah = ""

    }
}