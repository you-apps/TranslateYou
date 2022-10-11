package com.bnyro.translate.api.deepl.obj

data class DeeplTranslationResponse(
    val translations: List<DeeplTranslation> = listOf()
)
