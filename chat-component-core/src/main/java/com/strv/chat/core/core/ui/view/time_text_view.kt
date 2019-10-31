package com.strv.chat.core.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.strv.chat.core.R
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Components that displays time of a date.
 */
class TimeTextView : TextView {

    /**
     * Date to be displayed in its time representation.
     */
    var date: Date? = null
        set(value) {
            value?.let { date ->
                text = getTime(date)
            }
            field = value
        }

    //constructors
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * Returns time of the date.
     *
     * @param date Date to get time of.
     *
     * @return If [date] represents time within one minute, returns "Now.
     *         Otherwise returns the date in "HH:mm" format.
     */
    private fun getTime(date: Date): String {
        val now = Date()
        val diff = now.time - date.time

        return if (diff < 60000) {
            context.getString(R.string.now)
        } else {
            val dateFormat = SimpleDateFormat("HH:mm")
            dateFormat.format(date)
        }
    }
}