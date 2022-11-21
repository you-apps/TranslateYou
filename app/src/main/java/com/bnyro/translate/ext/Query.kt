package com.bnyro.translate.ext

fun query(dbQuery: () -> Unit) {
    Thread {
        dbQuery.invoke()
    }.start()
}
