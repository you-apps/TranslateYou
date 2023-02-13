package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplLanguage(
    val language: String = "",
    val name: String = ""
)
