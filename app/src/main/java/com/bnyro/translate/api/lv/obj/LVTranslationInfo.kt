package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class LVTranslationInfo(
    val definitions: List<LVDefinition> = listOf(),
    val examples: List<String> = listOf(),
    val extraTranslations: List<ExtraTranslation> = listOf(),
    val pronunciation: LVPronunciation? = null,
    val similar: List<String> = listOf(),
    val detectedSource: String? = null
)
