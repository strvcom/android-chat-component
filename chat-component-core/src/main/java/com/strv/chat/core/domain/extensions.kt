package com.strv.chat.core.domain

import java.util.Calendar
import java.util.Date
import java.util.LinkedList

/**
 * Perform an action for each item of the [LinkedList] and remove it
 *
 * @param action an action to be performed on an item of the [LinkedList] before it gets removed
 */
internal inline fun <T> LinkedList<T>.collect(action: (T) -> Unit) {
    while (isNotEmpty()) {
        action(pop())
    }
}

/**
 * Runs an action on the [List] if is not empty
 *
 * @param action an action to be performed on the [List]
 */
internal inline fun <T> List<T>.runNonEmpty(action: List<T>.() -> List<T>): List<T> =
    if (isNotEmpty()) action() else this

/**
 * Compare two days without considering time of the day
 *
 * @param other a day to be compared
 *
 * @return true if days are equals otherwise false
 */
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