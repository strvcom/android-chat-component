package com.strv.chat.core.domain

import java.util.Calendar
import java.util.Date
import java.util.LinkedList

internal inline fun <T> LinkedList<T>.collect(block: (T) -> Unit) {
    while (isNotEmpty()) {
        block(pop())
    }
}

internal inline fun <T> List<T>.runNonEmpty(block: List<T>.() -> List<T>): List<T> =
    if (isNotEmpty()) block() else this

internal fun Date.isDayEqual(other: Date): Boolean {
    val calendarDate = Calendar.getInstance().apply {
        time = this@isDayEqual
    }

    val otherCalendarDate = Calendar.getInstance().apply {
        time = other
    }

    return calendarDate.get(Calendar.DAY_OF_YEAR) == otherCalendarDate.get(Calendar.DAY_OF_YEAR) && calendarDate.get(
        Calendar.YEAR
    ) == otherCalendarDate.get(Calendar.YEAR)
}