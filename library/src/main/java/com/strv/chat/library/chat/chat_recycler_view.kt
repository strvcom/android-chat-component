package com.strv.chat.library.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.data.model.ChatItem
import com.strv.chat.library.domain.ChatClient

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ChatClient.Listener {

    private var chatClient: ChatClient? = null

    private val chatAdapter: ListAdapter<ChatItem, ViewHolder>?
        get() = adapter as ListAdapter<ChatItem, ViewHolder>

    override fun onMessagesChanged(chatItems: List<ChatItem>) {
        chatAdapter?.submitList(chatItems)
    }

    override fun onMessagesFetchFailed(exception: Throwable) {
        Toast.makeText(context, "Error has occured", Toast.LENGTH_SHORT).show()
    }

    fun startObserving() {
        requireNotNull(chatClient) { "ChatClient is not attached" }.run {
            registerListener(this@ChatRecyclerView)
            getMessages()
        }
    }

    fun stopObserving() {
        chatClient?.unregisterListener(this)
    }
}