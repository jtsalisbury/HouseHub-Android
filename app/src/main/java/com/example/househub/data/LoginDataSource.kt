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
                // TODO: handle failed login
            }

            val retrievedInfo = jwt.decodePayload(success)

            val user = LoggedInUser(retrievedInfo["fname"].toString(), retrievedInfo["lname"].toString(), retrievedInfo["fname"].toString() + " " + retrievedInfo["lname"], retrievedInfo["email"].toString(), retrievedInfo["admin"].toString(), retrievedInfo["created"].toString(), retrievedInfo["uid"].toString().toInt())
            return Result.Success(user)

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

