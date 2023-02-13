package com.bnyro.translate.api.reverso.obj

import kotlinx.serialization.Serializable

@Serializable
data class ReversoTranslationResponse(
    val contextResults: ReversoContextResults? = null,
    val correctedText: String? = null,
    val engines: List<String>? = null,
    val from: String? = null,
    val id: String? = null,
    val input: List<String>? = null,
    val languageDetection: ReversoLanguageDetection? = null,
    val timeTaken: Int? = null,
    val to: String? = null,
    val translation: List<String> = listOf(""),
    val truncated: Boolean? = null
)
