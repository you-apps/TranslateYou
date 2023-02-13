package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslationResponse(
    val translations: List<DeeplTranslation> = listOf()
)
