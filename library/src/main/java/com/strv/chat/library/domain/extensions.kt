package com.strv.chat.library.domain

import java.util.*

inline fun <T> List<T>.runNonEmpty(block: List<T>.() -> List<T>): List<T> =
    if (isNotEmpty()) block() else this

internal fun Date.isDayEqual(other: Date): Boolean {
    val calendarDate = Calendar.getInstance().apply {
        time = this@isDayEqual
    }

    val otherCalendarDate = Calendar.getInstance().apply  {
        time = other
    }

    return calendarDate.get(Calendar.DAY_OF_YEAR) == otherCalendarDate.get(Calendar.DAY_OF_YEAR) && calendarDate.get(
        Calendar.YEAR) == otherCalendarDate.get(Calendar.YEAR)
}