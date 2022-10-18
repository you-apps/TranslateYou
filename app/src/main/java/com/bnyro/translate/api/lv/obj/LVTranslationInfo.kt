package com.bnyro.translate.api.lv.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LVTranslationInfo(
    val definitions: List<LVDefinition> = listOf(),
    val examples: List<String> = listOf(),
    val extraTranslations: List<ExtraTranslation> = listOf(),
    val pronunciation: LVPronunciation? = null,
    val similar: List<String> = listOf(),
    val detectedSource: String? = null
)
