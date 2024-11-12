package com.aigirlfriend.virtuallove.utils

import java.security.MessageDigest

object MD5 {
    fun calculate(string: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(string.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
