package com.bnyro.translate.ext

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toastFromMainThread(text: String) {
    Handler(Looper.getMainLooper())
        .post {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
}

fun Context.toastFromMainThread(@StringRes textResource: Int) {
    toastFromMainThread(getString(textResource))
}
