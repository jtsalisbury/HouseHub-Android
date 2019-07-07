package com.example.househub.data

import com.google.gson.Gson
import java.util.*

class JWT {
    private var key = "";
    private var signAlgo = "";

    private var payloadSecret = "";
    private var payloadCipherAlgo = "";

    constructor (signKey: String, signAlgorithm: String, sec: String, ciph: String) {
        key = signKey;
        signAlgo = signAlgorithm;

        payloadSecret = sec;
        payloadCipherAlgo = ciph;
    }

    public fun encode64 (data: String): String {
        return Base64.getUrlEncoder().encodeToString(data.toByteArray());
    }

    public fun decode64 (data: String): String {
        return Base64.getUrlDecoder().decode(data).toString();
    }

    // generateToken(*payload)
    public fun generateToken(payload: List<String>) {
        var gson: Gson = Gson();

        var payload_json: String = gson.toJson(payload);
        
    }

    public fun verifyToken(token: String) {

    }

    public fun decodePayload(token: String): List<String> {

    }
}