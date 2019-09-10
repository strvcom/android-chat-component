package com.strv.chat.library.ui.view

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.widget.TextView
import com.strv.chat.library.R
import java.text.SimpleDateFormat
import java.util.*

private const val DAY = 60000 * 60 * 24

class RelativeTimeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {

    var date: Date? = null
        set(value) {
            value?.let { date ->
                text = getRelativeString(date)
            }
            field = value
        }

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
                context.getString(R.string.today)
            } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) { //is yesterday
                context.getString(R.string.yesterday)
            } else { //other
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            }

        } else {
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }
}