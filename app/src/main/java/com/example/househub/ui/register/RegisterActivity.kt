package com.example.househub.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns
import android.widget.Toast
import com.example.househub.HTTPRequest
import com.example.househub.JWT
import com.example.househub.R
import com.example.househub.data.Result
import com.example.househub.data.model.LoggedInUser
import com.example.househub.ui.listings.ListViewToolbarActivity
import com.example.househub.ui.login.LoginActivity
import com.example.househub.ui.login.afterTextChanged
import kotlinx.android.synthetic.main.activity_login.*

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class RegisterActivity : AppCompatActivity() {

    private lateinit var user: Map<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var registerSuccess = false

        submitButton.setOnClickListener {
            if(!registerFirstName.text.isNullOrEmpty() &&
                !registerLastName.text.isNullOrEmpty() &&
                !registerEmail.text.isNullOrEmpty() &&
                !registerPassword.text.isNullOrEmpty() &&
                    !registerConfirmPassword.text.isNullOrEmpty() &&
                isEmailValid(registerEmail.text.toString())) {

                val jwt = JWT()
                val payload = mapOf("fname" to registerFirstName.text.toString(),
                    "lname" to registerLastName.text.toString(),
                    "email" to registerEmail.text.toString(),
                    "pass" to registerPassword.text.toString(),
                    "repass" to registerConfirmPassword.text.toString())

                val url = "http://u747950311.hostingerapp.com/househub/api/user/create.php"
                var success = ""
                var fail = ""

                val deferred = GlobalScope.async {
                    val httpRequest = HTTPRequest(url, payload, success, fail)
                    val results = httpRequest.open()

                    success = results.first
                    fail = results.second

                    if(fail != "") {
                        // TODO: handle failed login
                        Toast.makeText(
                            applicationContext,
                            fail,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
                        registerSuccess = true
                    }

                    val retrievedInfo = jwt.decodePayload(success)
                    user = retrievedInfo
                }

                runBlocking {
                    while(!deferred.isCompleted) {
                        delay(10)
                    }

                    if(registerSuccess) {
                        val retrievedInfo = jwt.decodePayload(success)

                        LoginActivity.userIdGlobal = retrievedInfo["uid"].toString().toInt()
                        LoginActivity.firstNameGlobal = retrievedInfo["fname"].toString()
                        LoginActivity.lastNameGlobal = retrievedInfo["lname"].toString()
                        LoginActivity.emailGlobal = retrievedInfo["email"].toString()
                        LoginActivity.adminGlobal = 0
                        LoginActivity.displayNameGlobal = LoginActivity.firstNameGlobal + " " + LoginActivity.lastNameGlobal
                        LoginActivity.createdDateGlobal = LocalDateTime.now().toString()

                        val intent = Intent(baseContext, ListViewToolbarActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        val failedString = "Registration failed: " + fail
                        Toast.makeText(
                            applicationContext,
                            failedString,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
            else {
                if(isEmailValid(registerEmail.text.toString())) {
                    Toast.makeText(
                        applicationContext,
                        "All fields are required to register.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else {
                    Toast.makeText(
                        applicationContext,
                        "Enter a valid email address.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.toRegex().matches(email)
    }
}
