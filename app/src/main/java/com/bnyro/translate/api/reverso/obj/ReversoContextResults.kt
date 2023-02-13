package com.bnyro.translate.api.reverso.obj

import kotlinx.serialization.Serializable

@Serializable
data class ReversoContextResults(
    val colloquialisms: Boolean? = null,
    val results: List<ReversoResult>? = null,
    val riskyWords: Boolean? = null,
    val rudeWords: Boolean? = null,
    val timeTakenContext: Int? = null,
    val totalContextCallsMade: Int? = null
)
