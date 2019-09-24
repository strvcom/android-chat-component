package com.strv.chat.library.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.session.ChatComponent.chatClient
import com.strv.chat.library.core.session.ChatComponent.defaultChatItemBinder
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.mapper.chatItemView
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatItemBinder
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.map
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MemberProvider
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

    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider

    init {
        addOnLayoutChangeListener { v, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
               // postDelayed({ scrollToPosition(0) }, 100)
            }
        }
    }

    fun init(
        conversationProvider: ConversationProvider,
        memberProvider: MemberProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(conversationProvider, memberProvider).apply(config).build()

        addOnScrollListener(object : PaginationListener(layoutManager as LinearLayoutManager) {
            override fun loadMoreItems(offset: Int) {
                loadMoreMessages(chatAdapter.getItem(offset).sentDate)
            }
        })
    }

    fun onStart(): ObservableTask<List<ChatItemView>, Throwable> =
        chatClient().subscribeMessages(
            conversationProvider.conversationId
        ).onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }.onNext { response ->
            chatClient().setSeen(
                memberProvider.currentUserId(),
                conversationProvider.conversationId,
                response.first()
            ).also { task ->
                disposable.add(task)
            }
        }.map { model ->
            chatItemView(model, memberProvider)
        }.onNext { itemViews ->
            onMessagesChanged(itemViews)
        }.also { task ->
            disposable.add(task)
        }

    fun onStop() {
        while (disposable.isNotEmpty()) {
            disposable.pop().dispose()
        }
    }

    private fun loadMoreMessages(startAfter: Date) {
        disposable.add(
            chatClient().messages(
                conversationProvider.conversationId, startAfter
            ).map { model ->
                chatItemView(model, memberProvider)
            }.onSuccess { response ->
                chatAdapter.submitList(chatAdapter.getItems().plus(response))
            }.onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
            }
        )
    }

    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter.run {
            submitList(items)
            if (items.isNotEmpty()) postDelayed({ smoothScrollToPosition(0) }, 0)
        }
    }

    inner class Builder(
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider,
        var binder: ChatItemBinder? = null,
        var layoutManager: LinearLayoutManager? = null
    ) {

        fun build() {
            adapter = ChatAdapter(binder ?: defaultChatItemBinder())
            setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = false
                setClipToPadding(false)
            })
            this@ChatRecyclerView.conversationProvider = conversationProvider
            this@ChatRecyclerView.memberProvider = memberProvider
        }
    }
}

