package com.bnyro.translate.api.lv.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExtraTranslationItem(
    val article: String? = null,
    val frequency: Int? = null,
    val meanings: List<String>? = null,
    val word: String? = null
)
