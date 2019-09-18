package com.strv.chat.library.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.map
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.chat.mapper.chatItemView
import com.strv.chat.library.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.library.ui.chat.messages.adapter.ChatItemBinder
import com.strv.chat.library.ui.chat.messages.adapter.DefaultChatItemBinder
import strv.ktools.logE
import java.util.*

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val disposable = LinkedList<Disposable>()

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

    fun startObserving(): ObservableTask<List<ChatItemView>, Throwable> =
            chatClient.subscribeMessages(
                conversationProvider.conversationId
            ).onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
            }.onNext { response ->
                chatClient.setSeen(
                    memberProvider.currentUserId(),
                    conversationProvider.conversationId,
                    response.first()
                )
            }.map { model ->
                chatItemView(model, memberProvider)
            }.onNext { itemViews ->
                onMessagesChanged(itemViews)
            }.also { task ->
                disposable.add(task)
            }

    fun stopObserving() {
        while (disposable.isNotEmpty()) {
            disposable.pop().dispose()
        }
    }

    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter.run {
            submitList(items)
            if (items.isNotEmpty()) postDelayed({ scrollToPosition(0) }, 50)
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
            //todo consider using component root provider
            adapter = ChatAdapter(binder ?: DefaultChatItemBinder())
            this@ChatRecyclerView.chatClient = chatClient
            this@ChatRecyclerView.conversationProvider = conversationProvider
            this@ChatRecyclerView.memberProvider = memberProvider
        }
    }
}

