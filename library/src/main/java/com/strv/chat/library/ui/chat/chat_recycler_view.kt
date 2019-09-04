package com.strv.chat.library.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.ChatClient
import com.strv.chat.library.domain.MessagesObserver
import com.strv.chat.library.domain.model.ChatItem
import strv.ktools.logE

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var chatClient: ChatClient? = null

    private val chatAdapter: ListAdapter<ChatItem, ViewHolder>?
        get() = adapter as ListAdapter<ChatItem, ViewHolder>

    private val messagesObserver = object: MessagesObserver {
        override fun onComplete(list: List<ChatItem>) {
            onMessagesChanged(list)
        }

        override fun onNext(list: List<ChatItem>) {
            onMessagesChanged(list)
        }

        override fun onError(error: Throwable) {
            onMessagesFetchFailed(error)
        }
    }

    init {
        layoutManager = LinearLayoutManager(context).apply {
            reverseLayout = true
        }
        adapter = ChatAdapter()

    }

    fun chatClient(config: () -> ChatClient) {
        this.chatClient = config()
    }

    fun startObserving(observer: MessagesObserver = messagesObserver) {
        requireNotNull(chatClient) { logE("ChatClient is not attached") }.run {
            subscribeMessages(observer = observer)
        }
    }

    fun stopObserving() {
        chatClient?.unsubscribeMessages()
    }

    private fun onMessagesChanged(chatItems: List<ChatItem>) {
        chatAdapter?.submitList(chatItems)
    }

    private fun onMessagesFetchFailed(exception: Throwable) {
        Toast.makeText(context, "Error has occured", Toast.LENGTH_SHORT).show()
    }
}