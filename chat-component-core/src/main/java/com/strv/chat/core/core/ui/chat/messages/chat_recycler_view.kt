package com.strv.chat.core.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.session.ChatComponent
import com.strv.chat.core.core.session.ChatComponent.chatAdapter
import com.strv.chat.core.core.session.ChatComponent.chatClient
import com.strv.chat.core.core.session.ChatComponent.memberProvider
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.data.mapper.chatItemView
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewHolderProvider
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.Disposable
import com.strv.chat.core.domain.ObservableTask
import com.strv.chat.core.domain.map
import strv.ktools.logE
import java.util.Date
import java.util.LinkedList

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

    private val onFirstLayoutChangeListener = object : OnLayoutChangeListener {
        override fun onLayoutChange(
            p0: View?,
            p1: Int,
            p2: Int,
            bottom: Int,
            p4: Int,
            p5: Int,
            p6: Int,
            p7: Int,
            oldBottom: Int
        ) {
            if (bottom <= oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                scrollToPosition(0)

                removeOnLayoutChangeListener(this)
                addOnLayoutChangeListener(onLayoutChangeListener)
            }
        }
    }

    private val onLayoutChangeListener =
        OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                postDelayed( { scrollToPosition(0) }, 50)
            }
        }

    private var style: ChatRecyclerViewStyle? = null

    init {
        addOnLayoutChangeListener(onFirstLayoutChangeListener)

        if (attrs != null) {
            style = ChatRecyclerViewStyle.parse(context, attrs)
        }
    }

    @JvmOverloads
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

