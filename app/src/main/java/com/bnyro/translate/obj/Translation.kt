package com.bnyro.translate.obj

data class Translation(
    val translatedText: String,
    val detectedLanguage: String? = null,
    val definitions: List<Definition>? = null,
    val similar: List<String>? = null,
    val examples: List<String>? = null
)
