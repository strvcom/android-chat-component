package com.strv.chat.library.chat.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.chat_recycler_view, this)
    }

   // fun adapter()

}