package com.bnyro.translate.api.reverso.obj

data class ReversoRequestOptions(
    val contextResults: Boolean = true,
    val languageDetection: Boolean = true,
    val origin: String = "reversomobile",
    val sentenceSplitter: Boolean = false
)
