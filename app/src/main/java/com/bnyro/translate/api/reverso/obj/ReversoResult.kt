package com.bnyro.translate.api.reverso.obj

import kotlinx.serialization.Serializable

@Serializable
data class ReversoResult(
    val colloquial: Boolean? = null,
    val frequency: Int? = null,
    val partOfSpeech: String? = null,
    val rude: Boolean? = null,
    val sourceExamples: List<String> = listOf(),
    val targetExamples: List<String> = listOf(),
    val translation: String? = null
)
