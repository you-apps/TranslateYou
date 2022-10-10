package com.bnyro.translate.util

fun getStepCount(minValue: Float, maxValue: Float, stepSize: Float): Int {
    return ((maxValue - minValue) / stepSize).toInt() - 1
}
