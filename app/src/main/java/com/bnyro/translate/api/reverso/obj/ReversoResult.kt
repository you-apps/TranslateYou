package com.bnyro.translate.api.reverso.obj

data class ReversoResult(
    val colloquial: Boolean,
    val frequency: Int,
    val partOfSpeech: String,
    val rude: Boolean,
    val sourceExamples: List<String>,
    val targetExamples: List<String>,
    val translation: String,
    val transliteration: Any,
    val vowels: Any
)