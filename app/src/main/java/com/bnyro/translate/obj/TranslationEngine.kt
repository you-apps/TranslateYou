package com.bnyro.translate.obj

import com.bnyro.translate.api.APIHelper

data class TranslationEngine(
    val id: Int,
    val name: String,
    val defaultUrl: String,
    val apiHelper: APIHelper,
    val urlModifiable: Boolean,
    val apiKeyState: Int
)
