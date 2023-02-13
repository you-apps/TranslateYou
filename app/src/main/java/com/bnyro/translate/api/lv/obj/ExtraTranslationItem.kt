package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class ExtraTranslationItem(
    val article: String? = null,
    val frequency: Int? = null,
    val meanings: List<String>? = null,
    val word: String? = null
)
