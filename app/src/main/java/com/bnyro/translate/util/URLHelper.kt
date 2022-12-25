package com.bnyro.translate.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object URLHelper {
    private val charset = StandardCharsets.UTF_8.toString()

    suspend fun encodeURL(text: String): String {
        return withContext(Dispatchers.IO) {
            URLEncoder.encode(text, charset)
        }
    }

    suspend fun decodeURL(url: String): String {
        return withContext(Dispatchers.IO) {
            URLDecoder.decode(url, charset)
        }
    }
}
