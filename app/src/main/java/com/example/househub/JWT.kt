package com.example.househub

import com.example.househub.data.model.ListingPayload
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

    // Gets the payload from a token
    fun decodePayloadSublet(token: String): ListingPayload {
        val parts: Array<String> = token.split(".").toTypedArray()

        if (parts.size < 3) {
            //return mapOf("" to "")
        }

        val gson = Gson()

        var payload = parts[1]
        val decrypted = decrypt(payload)

        val m = object : TypeToken<ListingPayload>() {}.type

        payload = decode64(decrypted)

        val res: ListingPayload = gson.fromJson(payload, m);

        return res

        //val pages = gson.fromJson(string1, m) as Map<String, Any>

        // This works!  It's because there's an error for when trying to fromJson images for some reason.
        // I removed images from this request, which is why it works.
        /*var test = "{\"pid\":\"80\",\"title\":\"Luxurious Apartment Near UC\",\"desc\":\"Elegant custom home offers unparalleled craftsmanship and exceptional amenities! This French inspired design is truly remarkable inside and out. Features include cabinets, counter tops, custom windows provide plenty of natural lighting, kitchen with (no) island (great for entertaining), gorgeous master suite, storage, plus STUNNING views of Kroger. Fantastic selfie mirror allows for great lighting! Extremely functional pull-out couch provided. Parking not provided. Animals under 50lbs allowed. Available anytime.\",\"loc\":\"2508 Auburn Ave Cincinnati, OH 45219\",\"base_price\":\"400\",\"add_price\":\"Utilities\",\"creator_uid\":\"56\",\"num_pictures\":\"5\",\"created\":\"2019-07-25 18:17:52\",\"modified\":\"2019-07-25 18:17:52\",\"creator_fname\":\"JT\",\"creator_lname\":\"Salisbury\",\"creator_email\":\"crazyscouter1@gmail.com\",\"hidden\":\"0\",\"saved\":\"0\"}"
        val testResult = gson.fromJson(test, m) as Map<String, Any>

        val coolKidTest = "{\"pid\":\"80\",\"title\":\"Luxurious Apartment Near UC\",\"desc\":\"Elegant custom home offers unparalleled craftsmanship and exceptional amenities! This French inspired design is truly remarkable inside and out. Features include cabinets, counter tops, custom windows provide plenty of natural lighting, kitchen with (no) island (great for entertaining), gorgeous master suite, storage, plus STUNNING views of Kroger. Fantastic selfie mirror allows for great lighting! Extremely functional pull-out couch provided. Parking not provided. Animals under 50lbs allowed. Available anytime.\",\"loc\":\"2508 Auburn Ave Cincinnati, OH 45219\",\"base_price\":\"400\",\"add_price\":\"Utilities\",\"creator_uid\":\"56\",\"num_pictures\":\"5\",\"created\":\"2019-07-25 18:17:52\",\"modified\":\"2019-07-25 18:17:52\",\"creator_fname\":\"JT\",\"creator_lname\":\"Salisbury\",\"creator_email\":\"crazyscouter1@gmail.com\",\"hidden\":\"0\",\"saved\":\"0\"images\":[\"0.jpg\",\"1.jpg\",\"2.jpg\",\"3.jpg\",\"4.jpeg\"]}"
        val coolKidTestResult = gson.fromJson(coolKidTest, ListingData::class.java)

        val test2 = "{\"images\":\"0.jpg\"}"
        val testResult2 = gson.fromJson(test2, m) as Map<String, Any>

        val test3 = "{\"images\":[\"0.jpg\",\"1.jpg\",\"2.jpg\",\"3.jpg\",\"4.jpeg\"]}"
        val testResult3 = gson.fromJson(test3, m) as Map<String, Any>

        val test4 = "{\"hidden\":\"0\",\"saved\":\"0\",\"images\":[\"0.jpg\",\"1.jpg\",\"2.jpg\",\"3.jpg\",\"4.jpeg\"]}"
        val testResult4 = gson.fromJson(test2, m) as Map<String, Any>*/

        // Get listings
       /* val listings = mutableListOf<Map<String, Any>>()
        var listingCount = 0
        pos = pos+12
        while(pos < string.length-2) {
            var char = string[pos].toString()
            string2+=char
            pos++
            if(char == "}" && string2.length > 30) {
                //listings[listingCount] = gson.fromJson(string2, n) as Map<String, Any>
                listingCount++
                pos++
                string2 = ""
            }
        }*/

        //val subletList= gson.fromJson(string2, n) as List<Map<String, Any>>
        //return Pair(pages, listings)
    }
}