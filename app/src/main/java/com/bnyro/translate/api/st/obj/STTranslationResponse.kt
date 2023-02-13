package com.bnyro.translate.api.st.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class STTranslationResponse(
    @SerialName("definitions")
    val definitions: STDefinition? = null,
    @SerialName("source_language")
    val source_language: String? = null,
    @SerialName("translated-text")
    val translated_text: String = ""
)
