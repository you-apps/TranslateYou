package com.bnyro.translate.api.lt.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LTLanguage(
    val code: String? = null,
    val name: String? = null,
    val targets: List<String> = listOf()
)
