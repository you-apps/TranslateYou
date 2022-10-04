package com.bnyro.translate.ext

fun Query(dbQuery: () -> Unit) {
    Thread {
        dbQuery.invoke()
    }.start()
}
