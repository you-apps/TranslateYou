package com.bnyro.translate.api.st.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class STAbbreviation(
    val definition: String? = null
)
