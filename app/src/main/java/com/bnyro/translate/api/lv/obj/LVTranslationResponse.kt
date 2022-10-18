package com.bnyro.translate.api.lv.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LVTranslationResponse(
    val info: LVTranslationInfo? = null,
    val translation: String = ""
)
