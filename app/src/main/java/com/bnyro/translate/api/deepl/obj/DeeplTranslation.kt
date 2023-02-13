package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslation(
    val detected_source_language: String = "",
    val text: String = ""
)
