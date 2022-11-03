package com.bnyro.translate.api.st.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class STDefinition(
    val abbreviation: List<STAbbreviation> = listOf(),
    val noun: List<Any> = listOf(),
    val verb: List<Any> = listOf()
)
