package com.example.househub.data.model

import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Test

class LoginTest {
    @Test
    fun testLogin() {
        val email = "test@gmail.com" // test@gmail.com
        val password = "test" //asdf

        val jwt = JWT()
        val payload = mapOf("email" to email, "pass" to password)

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