package com.bnyro.translate.api.lv.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExtraTranslation(
    val list: List<ExtraTranslationItem> = listOf(),
    val type: String = ""
)
