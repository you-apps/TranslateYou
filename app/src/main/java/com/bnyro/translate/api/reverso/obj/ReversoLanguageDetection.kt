package com.bnyro.translate.api.reverso.obj

data class ReversoLanguageDetection(
    val changedDirectionContextMatches: Int? = null,
    val detectedLanguage: String? = null,
    val isDirectionChanged: Boolean? = null,
    val originalDirection: String? = null,
    val originalDirectionContextMatches: Int? = null,
    val timeTaken: Int? = null
)
