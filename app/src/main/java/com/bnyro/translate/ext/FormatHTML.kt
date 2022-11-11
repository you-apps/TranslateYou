package com.bnyro.translate.ext

import android.os.Build
import android.text.Html
import androidx.compose.ui.text.AnnotatedString

fun String.formatHTML(): AnnotatedString {
    val html = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
    return html.toAnnotatedString()
}
