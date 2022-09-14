package com.bnyro.translate.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardHelper(context: Context) {
    private val clipboardManager = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    fun write(text: String) {
        val clip = ClipData.newPlainText(text, text)
        clipboardManager.setPrimaryClip(clip)
    }

    fun get(): String? {
        val clipboardItem = clipboardManager.primaryClip?.getItemAt(0) ?: return null
        return clipboardItem.text.toString()
    }
}
