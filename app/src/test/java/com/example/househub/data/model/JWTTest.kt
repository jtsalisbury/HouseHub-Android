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

    @Test
    fun testSubletDecodePayload() {
        val jwt: JWT = JWT();


        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.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.NjQ5ODUwNjlhZTZhNWNmNTg5Y2Q1ZTkyNzNiMWNjZTFhODlkZWZhZjM3OGQyMjIxNDU0MWU5NzZjMDQ5ZWFmYw=="
        val res: ListingPayload = jwt.decodePayloadSublet(token);

        val result = "";
    }
}