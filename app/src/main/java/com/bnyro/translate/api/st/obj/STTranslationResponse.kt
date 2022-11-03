package com.bnyro.translate.api.st.obj

import com.fasterxml.jackson.annotation.JsonProperty

data class STTranslationResponse(
    @JsonProperty("definitions")
    val definitions: STDefinition? = null,
    @JsonProperty("source_language")
    val source_language: String? = null,
    @JsonProperty("translated-text")
    val translated_text: String = "",
    @JsonProperty("translations")
    val translations: STTranslations? = null
)
