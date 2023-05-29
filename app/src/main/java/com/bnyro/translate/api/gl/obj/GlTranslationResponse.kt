package com.bnyro.translate.api.gl.obj

import kotlinx.serialization.Serializable

@Serializable
data class GlTranslationResponse(
    val input: String,
    val translation: String
)
