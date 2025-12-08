package org.tiffinservice.app

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

@OptIn(ExperimentalTime::class)
fun Long.toDateFormatted(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dt.dayOfMonth} ${dt.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dt.year}"
}

@OptIn(ExperimentalTime::class)
fun Long.toDateTimeFormatted(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dt.hour % 12
    val amPm = if (dt.hour >= 12) "PM" else "AM"
    return "${dt.dayOfMonth} ${dt.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dt.year}, $hour:${dt.minute.toString().padStart(2,'0')} $amPm"
}
