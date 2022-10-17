package com.bnyro.translate.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

class ClipboardHelper(context: Context) {
    private val clipboardManager = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    fun write(text: String) {
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText(text, text)
        )
    }

    fun get(): String? {
        val clipboardItem = clipboardManager.primaryClip?.getItemAt(0) ?: return null
        return clipboardItem.text.toString()
    }

    fun clear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clipboardManager.clearPrimaryClip()
        } else {
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText("", "")
            )
        }
    }
}
