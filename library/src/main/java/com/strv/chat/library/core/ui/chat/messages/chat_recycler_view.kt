package com.strv.chat.library.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.session.ChatComponent
import com.strv.chat.library.core.session.ChatComponent.chatAdapter
import com.strv.chat.library.core.session.ChatComponent.chatClient
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image
import com.strv.chat.library.core.ui.chat.data.mapper.chatItemView
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewHolderProvider
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.OnClickAction
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.map
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MemberProvider
import strv.ktools.logE
import java.util.*

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val disposable = LinkedList<Disposable>()

    private var chatAdapter: ChatAdapter
        get() = super.getAdapter() as ChatAdapter
        private set(value) = super.setAdapter(value)

    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider

    private var onImageClick: OnClickAction<Image>? = null

    private val onFirstLayoutChangeListener = object : OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            p1: Int,
            p2: Int,
            p3: Int,
            bottom: Int,
            p5: Int,
            p6: Int,
            p7: Int,
            oldBottom: Int
        ) {
            if (bottom <= oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                scrollToPosition(0)
                removeOnLayoutChangeListener(this)
            }
        }
    }

    private var style: ChatRecyclerViewStyle? = null

    init {
        addOnLayoutChangeListener(onFirstLayoutChangeListener)

        if (attrs != null) {
            style = ChatRecyclerViewStyle.parse(context, attrs)
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
            chatItemView(model, memberProvider, onImageClick)
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
            if (items.isNotEmpty()) postDelayed({ smoothScrollToPosition(0) }, 100)
        }
    }

    inner class Builder(
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider,
        var viewHolderProvider: ChatViewHolderProvider = ChatComponent.chatViewHolderProvider(),
        var onImageClick: OnClickAction<Image>? = null,
        var layoutManager: LinearLayoutManager? = null
    ) {

        fun build() {
            chatAdapter = chatAdapter(viewHolderProvider, style)
            setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
                setClipToPadding(false)
            })
            this@ChatRecyclerView.conversationProvider = conversationProvider
            this@ChatRecyclerView.memberProvider = memberProvider
            this@ChatRecyclerView.onImageClick = onImageClick
        }
    }
}

