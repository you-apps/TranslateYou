package com.bnyro.translate.api.lt.obj

import kotlinx.serialization.Serializable

@Serializable
data class LTLanguage(
    val code: String? = null,
    val name: String? = null,
    val targets: List<String> = listOf()
)
