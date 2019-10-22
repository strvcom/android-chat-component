package com.strv.chat.core.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.strv.chat.core.R
import java.text.SimpleDateFormat
import java.util.Date

class TimeTextView : TextView {

    var date: Date? = null
        set(value) {
            value?.let { date ->
                text = getTime(date)
            }
            field = value
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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