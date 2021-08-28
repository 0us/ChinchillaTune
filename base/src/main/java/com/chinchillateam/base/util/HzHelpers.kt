package com.chinchillateam.base.util

import kotlin.math.ln
import kotlin.math.pow

fun Double.plusHalfSteps(halfSteps: Double): Double {
    return this * 2.0.pow(halfSteps / 12.0)
}

fun hzToPercent(hz: Double, min: Double, max: Double) = (ln(hz / min)) / (ln(max / min))

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}
