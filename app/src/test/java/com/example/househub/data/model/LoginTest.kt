package com.example.househub.data.model

import com.example.househub.HTTPRequest
import com.example.househub.JWT
import org.jetbrains.anko.doAsync
import org.junit.Test



class LoginTest {

    private var httpRequestTest: HTTPRequest? = null
    @Test
    fun testLogin() {
        val email = "test@gmail.com" // test@gmail.com
        val password = "test" //test

        val jwt = JWT()
        val payload = mapOf("email" to email, "pass" to password)

        val url = "http://u747950311.hostingerapp.com/househub/api/user/login.php"
        var success = ""
        var fail = ""

        doAsync {
            httpRequestTest = HTTPRequest(url, payload, success, fail)
        }

        val results = httpRequestTest!!.open()

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