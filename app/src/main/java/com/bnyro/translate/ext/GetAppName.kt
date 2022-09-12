package com.bnyro.translate.ext

import android.content.Context

fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()
