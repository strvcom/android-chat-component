package com.strv.chat.library.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.strv.chat.library.R
import java.text.SimpleDateFormat
import java.util.*

class TimeTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {

    var date: Date? = null
        set(value) {
            value?.let { date ->
                text = getTime(date)
            }
            field = value
        }

    private fun getTime(date: Date): String {
        val now = Date()
        val diff = now.time - date.time

        return if (diff < 60000) {
            context.getString(R.string.now)
        } else {
            var str: String? = null
            val dateFormat = SimpleDateFormat("HH:mm")
            str = dateFormat.format(date)
            str
        }
    }
}