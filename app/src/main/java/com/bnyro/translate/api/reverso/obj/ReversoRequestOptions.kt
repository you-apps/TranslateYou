package com.bnyro.translate.api.reverso.obj

import kotlinx.serialization.Serializable

@Serializable
data class ReversoRequestOptions(
    val contextResults: Boolean = true,
    val languageDetection: Boolean = true,
    val origin: String = "reversomobile",
    val sentenceSplitter: Boolean = false
)
