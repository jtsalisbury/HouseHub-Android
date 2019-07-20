package com.example.househub.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class JWT {
    // This info would be private in a real release
    private var key = "17EK11tAAV9JIR3d/8wqo7sLBwzqUQWC";
    private var signAlgo = "HmacSHA256";

    private var payloadSecret = "dS2wyKlp3oqBUkZSZ7FIncRV5LSs9wiH";
    private var payloadCipherAlgo = "AES-128-CBC";

    public fun encode64 (data: String): String {
        return Base64.getUrlEncoder().encodeToString(data.toByteArray());
    }

    public fun decode64 (data: String): String {
        return String(Base64.getUrlDecoder().decode(data));
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(payloadSecret.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)

        val ciphertext = cipher.doFinal(data.toByteArray());

        return Base64.getEncoder().encodeToString(ciphertext);
    }

    public fun decrypt(data: String): String  {
        val rawKey : ByteArray = key.toByteArray();

        val skeySpec = SecretKeySpec(rawKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        val byteArray = cipher.doFinal(data.toByteArray())

        return byteArray.toString()
    }

    public fun hash(data: String): ByteArray = try {
       Mac.getInstance(signAlgo).run {
           init(SecretKeySpec(key.toByteArray(), signAlgo))
           doFinal(data.toByteArray());
       }
    } catch (e: Exception) {
        throw RuntimeException("Could not run HMAC SHA256", e);
    }

    // generateToken(*payload)
    public fun generateToken(payload: Map<String, String>) {
        var gson: Gson = Gson();

        var header = mapOf("typ" to "JWT", "alg" to "HS256");

        var header_enc = encode64(gson.toJson(header));
        var payload_enc = encode64(gson.toJson(payload));


        var payload_encrypted = encrypt(payload_enc);
        var hashedSign = String(hash(header_enc + payload_encrypted));

        var sign = encode64(hashedSign);

        var token = header_enc + "." + payload_encrypted + "." + sign;
    }

    public fun verifyToken(token: String): Boolean {
        val parts: Array<String> = token.split(".").toTypedArray();

        if (parts.size < 3) {
            return false;
        }

        var header = parts[0];
        var payload = parts[1];
        var signature = parts[2];

        var curSign = decode64(signature);
        var rawSign = String(hash(header + "." + payload));

        return (curSign == rawSign);
    }

    public fun decodePayload(token: String): Map<String, String> {
        var parts: Array<String> = token.split(".").toTypedArray();

        if (parts.size < 3) {
            return mapOf("" to "");
        }

        var gson: Gson = Gson();

        var payload = parts[1];
        var decrpted = decrypt(payload);

        var m = object : TypeToken<Map<String, String>>() {}.type

        return gson.fromJson(decode64(decrpted), m);
    }
}