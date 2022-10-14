package com.bnyro.translate.api.mm.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MMTranslationResponse(
    val exception_code: Any = 0,
    val matches: List<MMTranslation> = emptyList(),
    val mtLangSupported: Any = false,
    val quotaFinished: Boolean = false,
    val responderId: String = "",
    val responseData: MMResponseData = MMResponseData(),
    val responseDetails: String = "",
    val responseStatus: Int = 0
)
