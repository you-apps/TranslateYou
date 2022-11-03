package com.bnyro.translate.api.reverso.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReversoResult(
    val colloquial: Boolean? = null,
    val frequency: Int? = null,
    val partOfSpeech: String? = null,
    val rude: Boolean? = null,
    val sourceExamples: List<String> = listOf(),
    val targetExamples: List<String> = listOf(),
    val translation: String? = null,
    val transliteration: Any? = null,
    val vowels: Any? = null
)
