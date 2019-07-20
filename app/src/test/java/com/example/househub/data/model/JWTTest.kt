package com.example.househub.data.model

import com.example.househub.data.JWT
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

        val testString = "abcdefghijklmnopqrstuvwxyz1234567890";
        val encrypted = "PdWbrB2qWpxA0uYDy3WO0OKcnDYYhElV5DGhRJujn/7XuqIAiR36YzNuYVJUcboJ";
        val enc2 = jwt.encrypt(testString);
        //assertEquals(testString, jwt.decrypt(enc2));

        assertEquals(encrypted, enc2);

    }
}