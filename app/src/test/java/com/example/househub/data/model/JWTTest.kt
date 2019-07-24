package com.example.househub.data.model

import com.example.househub.JWT
import junit.framework.Assert.assertEquals
import org.junit.Test

class JWTTest {
    @Test
    fun testEncodeDecode() {
        val jwt: JWT = JWT();

        val testString = "abcdefghijklmnopqrstuvwxyz1234567890";
        val encoded = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY3ODkw";

        assertEquals(encoded, jwt.encode64(testString));
        assertEquals(testString, jwt.decode64(encoded));
    }

    @Test
    fun testEncryptDecrypt() {
        val jwt: JWT = JWT();

        val testString = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";
        val encrypted = "oTtuEm0bagYFUhNJ0dZyKusDjMBOXjN2uPTjEcb3YvMxZnbuY9EAFNFArDWtSEVlc+oQM9KBczChkD7ppEfIWISlzfw+9htkkxFeUIRBdoE=";

        val enc2 = jwt.encrypt(testString);
        assertEquals(encrypted, enc2);

        val dec2 = jwt.decrypt(enc2);

        assertEquals(testString, dec2);
    }

    @Test
    fun testHash() {
        val jwt: JWT = JWT();

        val testString = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";

        var testHash = jwt.hash(testString);
        var realHash = "77d782b6345e6c3903db397226c2e814ea2145afdcd2ad7d718452d83b477c07";

        assertEquals(realHash, testHash);
    }

    @Test
    fun testGenerateToken() {
        val jwt: JWT = JWT()

        val realToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.xc6B91TEdgoaYm7ZARLvGKVYWXWW4FG0Qk4ivGp2ZeY3lUMGK+r+65+EgyggDFOv.MDY1OThjN2FjZjA0NzE3OWFjODRlZDQ4MjExZWI0ZWVlNTRmOTk3NDIwODc1MjA2YjUyNDc0NjVjNjUwZGZkMQ==";

        var payload = mapOf("email" to "test", "pass" to "test");

        var testToken = jwt.generateToken(payload);
        assertEquals(testToken, realToken);
    }

    @Test
    fun testVerifyToken() {
        val jwt: JWT = JWT();

        val realToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.xc6B91TEdgoaYm7ZARLvGKVYWXWW4FG0Qk4ivGp2ZeY3lUMGK+r+65+EgyggDFOv.MDY1OThjN2FjZjA0NzE3OWFjODRlZDQ4MjExZWI0ZWVlNTRmOTk3NDIwODc1MjA2YjUyNDc0NjVjNjUwZGZkMQ==";

        assert(jwt.verifyToken(realToken));
        assert(!jwt.verifyToken("afds.asdf"));
    }

    @Test
    fun testDecodePayload() {
        val jwt: JWT = JWT();

        val payload = mapOf("email" to "test", "pass" to "test");
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.xc6B91TEdgoaYm7ZARLvGKVYWXWW4FG0Qk4ivGp2ZeY3lUMGK+r+65+EgyggDFOv.MDY1OThjN2FjZjA0NzE3OWFjODRlZDQ4MjExZWI0ZWVlNTRmOTk3NDIwODc1MjA2YjUyNDc0NjVjNjUwZGZkMQ==";

        assertEquals(payload, jwt.decodePayload(token));
    }
}