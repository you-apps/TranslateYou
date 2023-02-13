package com.bnyro.translate.api.mm.obj

import kotlinx.serialization.Serializable

@Serializable
data class MMTranslationResponse(
    val exception_code: Int? = null,
    val matches: List<MMTranslation>? = emptyList(),
    val mtLangSupported: Boolean = false,
    val quotaFinished: Boolean = false,
    val responderId: String = "",
    val responseData: MMResponseData? = null,
    val responseDetails: String = "",
    val responseStatus: Int = 0
)
