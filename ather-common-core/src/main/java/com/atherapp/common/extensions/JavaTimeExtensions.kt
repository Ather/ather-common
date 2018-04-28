package com.atherapp.common.extensions

import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

fun DayOfWeek.lowerCase(): String = this.getDisplayName(TextStyle.FULL, Locale.US)

fun LocalTime.to24HourString() = "${this.hour}:${this.minute}"

fun ZonedDateTime.min(otherInstant: ZonedDateTime?): ZonedDateTime {
    if (otherInstant == null) return this
    return if (this < otherInstant) this
    else otherInstant
}

fun ZonedDateTime.max(otherInstant: ZonedDateTime?): ZonedDateTime {
    if (otherInstant == null) return this
    return if (this > otherInstant) this
    else otherInstant
}

fun ZonedDateTime.yearsBetween(otherInstant: ZonedDateTime?): Int = this.year - (otherInstant?.year ?: 0)

fun ZoneId.toZoneString() = this.id