package com.example.househub

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class HTTPRequest(_url: String, _payload: Map<String, Any>, _callbackSuccess: String, _callbackFail: String) {
    private val url = URL(_url)
    private val payload = _payload
    private var callbackSuccess = _callbackSuccess
    private var callbackFail = _callbackFail
    var res = ""


    fun open(): Pair<String, String> {
        val jwt = JWT()

        val gson = Gson()
        val token = jwt.generateToken(payload)

        val tokenEncoded = gson.toJson(mapOf("token" to token))

        with (url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            connectTimeout = 300000
            connectTimeout = 300000
            doOutput = true

            val postData: ByteArray = tokenEncoded.toByteArray(StandardCharsets.UTF_8)

            setRequestProperty("charset", "utf-8")
            setRequestProperty("Content-length", postData.size.toString())
            setRequestProperty("Content-Type", "application/json")

            try {
                val outputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            } catch (exception: Exception) {

            }

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try {

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()

                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        it.close()

                        val responseStr = response.toString()

                        val m = object : TypeToken<Map<String, String>>() {}.type

                        try {
                            val obj: Map<String, String> = gson.fromJson(responseStr, m)
                            val result = obj["status"]
                            val message = obj["message"]

                            if(result == "error") {
                                if(message == null) {
                                    callbackFail = "Undefined failure."
                                }
                                else {
                                    callbackFail = message
                                }
                            }

                            if (result == "success") {
                                if(message == null)
                                {
                                    callbackSuccess = "Undefined success."
                                }
                                else {
                                    callbackSuccess = message
                                }
                            }

                        } catch (e: JsonParseException) {

                            // Object couldn't be parsed to a map
                            //callbackFail(responseStr);
                        }

                    }

                } catch (exception: Exception) {
                    throw Exception("Exception while push the notification  $exception.message")
                }
            }
        }
        return Pair(callbackSuccess,callbackFail)
    }
}