package com.strv.chat.library.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnLayoutChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.session.ChatComponent
import com.strv.chat.library.core.session.ChatComponent.chatAdapter
import com.strv.chat.library.core.session.ChatComponent.chatClient
import com.strv.chat.library.core.session.ChatComponent.memberProvider
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.mapper.chatItemView
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewHolderProvider
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.OnClickAction
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.map
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

    private lateinit var conversationId: String

    private val onLayoutChangeListener =
        OnLayoutChangeListener { v, p1, p2, p3, bottom, p5, p6, p7, oldBottom ->
            if (bottom <= oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                scrollToPosition(0)
            }
        }

    private var style: ChatRecyclerViewStyle? = null

    init {
        addOnLayoutChangeListener(onLayoutChangeListener)

        if (attrs != null) {
            style = ChatRecyclerViewStyle.parse(context, attrs)
        }
    }

    fun init(
        conversationId: String,
        onClickAction: OnClickAction<ChatItemView>,
        viewHolderProvider: ChatViewHolderProvider = ChatComponent.chatViewHolderProvider(),
        layoutManager: LinearLayoutManager? = null
    ) {
        chatAdapter = chatAdapter(viewHolderProvider, onClickAction, style)

        setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply {
            reverseLayout = true
            stackFromEnd = true
            setClipToPadding(false)
        })

        this@ChatRecyclerView.conversationId = conversationId

        addOnScrollListener(object : PaginationListener(getLayoutManager() as LinearLayoutManager) {
            override fun loadMoreItems(offset: Int) {
                loadMoreMessages(chatAdapter.getItem(offset).sentDate)
            }
        })
    }

    fun onStart(): ObservableTask<List<ChatItemView>, Throwable> =
        chatClient().subscribeMessages(
            conversationId
        ).onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }.onNext { response ->
            chatClient().setSeen(
                memberProvider().currentUserId(),
                conversationId,
                response.first()
            ).also { task ->
                disposable.add(task)
            }
        }.map { model ->
            chatItemView(model, memberProvider())
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
                conversationId, startAfter
            ).map { model ->
                chatItemView(model, memberProvider())
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
        }
    }
}

