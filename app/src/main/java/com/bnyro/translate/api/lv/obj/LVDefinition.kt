package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class LVDefinition(
    val list: List<LVOtherInfo> = listOf(),
    val type: String? = null
)
