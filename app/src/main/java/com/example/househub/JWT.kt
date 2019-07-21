package com.example.househub

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class JWT {

    // This info would be private in a real release
    private var key = "17EK11tAAV9JIR3d/8wqo7sLBwzqUQWC"
    private var signAlgo = "HmacSHA256"

    private var payloadSecret = "W91UeXVpJlDv4Ivz"
    private var payloadCipherAlgo = "AES/ECB/PKCS5Padding"

    // Helper function for encoding strings to base64
    fun encode64 (data: String): String {
        return Base64.getEncoder().encodeToString(data.toByteArray())
    }

    // Helper function for decoding strings from base64
    fun decode64 (data: String): String {
        return String(Base64.getDecoder().decode(data))
    }

    // Encrypts a string using AES-128-ECB encryption
    fun encrypt(data: String): String {
        val secret = SecretKeySpec(payloadSecret.toByteArray(), "AES")

        val dataBytes = data.toByteArray()

        val cipher = Cipher.getInstance(payloadCipherAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, secret)

        return Base64.getEncoder().encodeToString(cipher.doFinal(dataBytes))
    }

    // Decrypts a string using AES-128-ECB decryption
    fun decrypt(data: String): String  {
        val dataBytes = Base64.getDecoder().decode(data.toByteArray())

        val secret = SecretKeySpec(payloadSecret.toByteArray(), "AES")

        val cipher = Cipher.getInstance(payloadCipherAlgo)
        cipher.init(Cipher.DECRYPT_MODE, secret)

        return String(cipher.doFinal(dataBytes))
    }

    // Helper function to turn a byte array into a hex string
    private fun hex(data: ByteArray): String = data.fold(StringBuilder()) { acc, next ->
        acc.append(String.format("%02x", next))
    }.toString().toLowerCase()

    // Computes the hash of a byte array
    private fun realHash(data: ByteArray): ByteArray = try {
        Mac.getInstance(signAlgo).run {
            init(SecretKeySpec(key.toByteArray(), signAlgo))
            doFinal(data)
        }
    } catch (e: Exception) {
        throw RuntimeException("Could not run HMAC SHA256", e)
    }

    // Helper function to computer the hash of a string
    fun hash(data: String): String {
        val res = realHash(data.toByteArray())

        return hex(res)
    }

    // Generates the token given a specific payload
    fun generateToken(payload: Map<String, Any>): String {
        val gson = Gson()

        val header = mapOf("typ" to "JWT", "alg" to "HS256")

        val headerEnc = encode64(gson.toJson(header))
        val payloadEnc = encode64(gson.toJson(payload))

        val payloadEncrypted = encrypt(payloadEnc)
        val hashedSign = hash(headerEnc + "." + payloadEncrypted)

        val sign = encode64(hashedSign)

        return headerEnc + "." + payloadEncrypted + "." + sign
    }

    // Verifies a token as valid
    fun verifyToken(token: String): Boolean {
        val parts: Array<String> = token.split(".").toTypedArray()

        if (parts.size < 3) {
            return false
        }

        val header = parts[0]
        val payload = parts[1]
        val signature = parts[2]

        val curSign = decode64(signature)
        val rawSign = hash(header + "." + payload)

        return (curSign == rawSign)
    }

    // Gets the payload from a token
    fun decodePayload(token: String): Map<String, Any> {
        val parts: Array<String> = token.split(".").toTypedArray()

        if (parts.size < 3) {
            return mapOf("" to "")
        }

        val gson = Gson()

        val payload = parts[1]
        val decrpted = decrypt(payload)

        val m = object : TypeToken<Map<String, String>>() {}.type

        return gson.fromJson(decode64(decrpted), m)
    }
}