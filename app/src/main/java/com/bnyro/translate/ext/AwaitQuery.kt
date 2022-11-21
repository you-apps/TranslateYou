package com.bnyro.translate.ext

fun <T> awaitQuery(action: () -> T): T {
    var value: T? = null
    Thread {
        value = action.invoke()
    }.apply {
        start()
        join()
    }
    return value!!
}
