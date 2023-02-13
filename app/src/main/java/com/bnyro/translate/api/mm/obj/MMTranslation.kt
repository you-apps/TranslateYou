package com.bnyro.translate.api.mm.obj

import kotlinx.serialization.Serializable

@Serializable
data class MMTranslation(
    val id: String = "",
    val match: Int = 0,
    val quality: String = "",
    val segment: String = "",
    val source: String = "",
    val subject: String = "",
    val target: String = "",
    val translation: String = ""
)
