package org.tiffinservice.app

import kotlin.math.pow
import kotlin.math.round

/**
 * Format a Double with a fixed number of decimal places.
 * Works in Kotlin Multiplatform (no JVM dependencies).
 */
fun Double.formatFixed(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor

    val str = rounded.toString()
    val parts = str.split('.')
    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1) ?: ""

    return if (decimals > 0) {
        buildString {
            append(integerPart)
            append('.')
            append(decimalPart.padEnd(decimals, '0').take(decimals))
        }
    } else {
        integerPart
    }
}
