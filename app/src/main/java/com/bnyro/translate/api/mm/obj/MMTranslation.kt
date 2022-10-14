package com.bnyro.translate.api.mm.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MMTranslation(
    val id: String = "",
    val match: Int = 0,
    val quality: String = "",
    val reference: Any = "",
    val segment: String = "",
    val source: String = "",
    val subject: String = "",
    val target: String = "",
    val translation: String = ""
)
