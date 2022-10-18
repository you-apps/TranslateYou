package com.bnyro.translate.api.lv.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LVOtherInfo(
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<String> = listOf()
)
