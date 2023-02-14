package com.bnyro.translate.api.reverso.obj

import kotlinx.serialization.Serializable

@Serializable
data class ReversoRequestBody(
    val format: String = "text",
    val options: ReversoRequestOptions = ReversoRequestOptions(),
    val from: String,
    val input: String,
    val to: String
)
