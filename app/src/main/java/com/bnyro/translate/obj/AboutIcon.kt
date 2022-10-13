package com.bnyro.translate.obj

data class AboutIcon(
    val contentDescription: Int,
    val iconResourceId: Int,
    val href: String,
    val onClick: (() -> Unit)? = null
)
