package com.bnyro.translate.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

fun <T> Context.startActivity(cls: Class<T>) {
    (this as Activity).startActivity(
        Intent(
            this,
            cls
        )
    )
}
