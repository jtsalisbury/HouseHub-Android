package com.example.househub.data

import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.data.model.LoggedInUser
import com.google.gson.Gson
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val jwt: JWT = JWT()
            val gson = Gson()
            val payload = mapOf("email" to email, "pass" to password)
            val token = jwt.generateToken(payload)

            val token2 = mapOf("token" to token)
            val tokenEnc = gson.toJson(token2)
            if(!jwt.verifyToken(tokenEnc)) {
                // TODO: handle invalid token
            }

            val url = "http://u747950311.hostingerapp.com/househub/api/user/login.php"
            var success = ""
            var fail = ""
            val h = HTTPRequest(url, payload, success, fail)
            val results = h.open()
            success = results.first
            fail = results.second

            if(fail != "") {
                // TODO: handle failed login
            }

            val test = jwt.decodePayload(success)

            /*val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)*/
            val user = LoggedInUser(java.util.UUID.randomUUID().toString(), test["name"].toString())
            return Result.Success(user)
            //return test["id"].toString().toInt()

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

