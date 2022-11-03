package com.bnyro.translate.api.st.obj

import com.fasterxml.jackson.annotation.JsonProperty

data class STCombiningForm(
    val definition: String? = null,
    @JsonProperty("use-in-sentence") val useInSentences: String? = null
)
