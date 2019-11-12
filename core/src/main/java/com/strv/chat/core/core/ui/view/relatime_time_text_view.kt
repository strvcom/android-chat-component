package com.strv.chat.core.core.ui.view

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val DAY = 60000 * 60 * 24

/**
 * Components that displays a relative date text representation.
 */
class RelativeDateTextView : AppCompatTextView {

    /**
     * Date to be displayed in a relative date representation.
     */
    var date: Date? = null
        set(value) {
            value?.let { date ->
                text = getRelativeString(date)
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
     * Returns a relative date representation.
     *
     * @param date Date to display.
     *
     * @return If [date] represents today, returns "Today".
     *         If [date] represents yesterday, returns "Yesterday".
     *         If [date] represents a day within in a week, returns the name of the day.
     *         Otherwise, returns the date in "MMM d, yyyy" format.
     *
     */
    private fun getRelativeString(date: Date): String {
        val now = Date()
        val diff = now.time - date.time

        return if (diff < DAY * 6) {
            val calendar = Calendar.getInstance().also {
                it.time = date
            }

            val yesterday = Calendar.getInstance().also {
                it.add(Calendar.DAY_OF_YEAR, -1)
            }

            if (DateUtils.isToday(date.time)) { //is today
                chatComponent.string(R.string.today)
            } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
            ) { //is yesterday
                chatComponent.string(R.string.yesterday)
            } else { //other
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            }

        } else {
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }
}