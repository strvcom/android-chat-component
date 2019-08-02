package com.strv.chat.library.chat.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.business.ChatClient
import com.strv.chat.library.chat.domain.ChatItem

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ChatClient.Listener {

    private var chatAdapter: ListAdapter<ChatItem, ViewHolder>?
    get() = adapter as ListAdapter<ChatItem, ViewHolder>
    set(value) {
        adapter = value
    }

    init {
        View.inflate(context, R.layout.chat_recycler_view, this)
    }

    override fun onChatChanged(chatItems: List<ChatItem>) {
        chatAdapter?.submitList(chatItems)
    }



}