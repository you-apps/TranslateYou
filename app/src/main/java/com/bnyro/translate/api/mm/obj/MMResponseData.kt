package com.bnyro.translate.api.mm.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MMResponseData(
    val match: Int = 0,
    val translatedText: String = ""
)
