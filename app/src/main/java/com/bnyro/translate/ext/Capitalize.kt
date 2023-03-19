package com.bnyro.translate.ext

import java.util.*

fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase(
            Locale.getDefault()
        )
    } else {
        it.toString()
    }
}
