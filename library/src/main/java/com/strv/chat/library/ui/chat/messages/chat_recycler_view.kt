package com.strv.chat.library.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.chat.mapper.chatItemView
import com.strv.chat.library.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.library.ui.chat.messages.adapter.ChatItemBinder
import com.strv.chat.library.ui.chat.messages.adapter.DefaultChatItemBinder

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var chatAdapter: ChatAdapter
        get() = super.getAdapter() as ChatAdapter
        private set(value) = super.setAdapter(value)

    private lateinit var chatClient: ChatClient
    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider

    init {
        addOnLayoutChangeListener { v, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }
    }

    operator fun invoke(
        chatClient: ChatClient,
        conversationProvider: ConversationProvider,
        memberProvider: MemberProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(chatClient, conversationProvider, memberProvider).apply(config).build()
    }

    fun startObserving(observer: Observer<List<ChatItemView>>) {
        chatClient.subscribeMessages(
            conversationProvider.conversationId,
            observer.convert { response ->
                chatClient.setSeen(
                    memberProvider.currentUserId(),
                    conversationProvider.conversationId,
                    response.first()
                )

                chatItemView(response, memberProvider).also(::onMessagesChanged)
            })
    }

    fun stopObserving() {
        chatClient.unsubscribeMessages()
    }

    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter.run {
            submitList(items)
            if (items.isNotEmpty()) smoothScrollToPosition(0)
        }
    }

    inner class Builder(
        val chatClient: ChatClient,
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider,
        var binder: ChatItemBinder? = null,
        var layoutManager: LinearLayoutManager? = null
    ) {

        fun build() {
            setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
                setClipToPadding(false)
            })
            adapter = ChatAdapter(binder ?: DefaultChatItemBinder())
            this@ChatRecyclerView.chatClient = chatClient
            this@ChatRecyclerView.conversationProvider = conversationProvider
            this@ChatRecyclerView.memberProvider = memberProvider
        }
    }
}

