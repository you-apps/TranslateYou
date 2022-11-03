package com.bnyro.translate.api.reverso.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReversoContextResults(
    val colloquialisms: Boolean? = null,
    val results: List<ReversoResult>? = null,
    val riskyWords: Boolean? = null,
    val rudeWords: Boolean? = null,
    val timeTakenContext: Int? = null,
    val totalContextCallsMade: Int? = null
)
