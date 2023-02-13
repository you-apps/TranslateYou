package com.bnyro.translate.api.st.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class STTranslations(
    val interjection: Any? = null
)
