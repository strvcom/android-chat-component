package com.strv.chat.core.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.data.creator.ChatItemViewListConfiguration
import com.strv.chat.core.core.ui.chat.data.creator.ChatItemViewListCreator
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatAdapter
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewHolderProvider
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.Disposable
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.ObservableTask
import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.map
import com.strv.chat.core.domain.model.IMemberModel
import strv.ktools.logE
import java.util.Date
import java.util.LinkedList

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var viewHolderProvider: ChatViewHolderProvider
        get() = throw UnsupportedOperationException("")
        set(value) {
            _viewHolderProvider = value
        }

    var onMessageClick: OnClickAction<ChatItemView>
        get() = throw UnsupportedOperationException("")
        set(value) {
            _onMessageClick = value
        }

    var imageLoader: ImageLoader
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageLoader = value
        }

    private var _viewHolderProvider: ChatViewHolderProvider =
        chatComponent.chatViewHolderProvider()

    private var _onMessageClick: OnClickAction<ChatItemView>? = null
    private var _imageLoader: ImageLoader? = null

    private val disposable = LinkedList<Disposable>()

    private var chatAdapter: ChatAdapter
        get() = super.getAdapter() as ChatAdapter
        private set(value) = super.setAdapter(value)

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
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }

    private var style: ChatRecyclerViewStyle? = null

    init {
        addOnLayoutChangeListener(onFirstLayoutChangeListener)

        if (attrs != null) {
            style = ChatRecyclerViewStyle.parse(context, attrs)
        }
    }

    fun init(builder: ChatRecyclerView.() -> Unit) {
        builder()

        if (layoutManager == null) {
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = false
                setClipToPadding(false)
            }
        }

        if (adapter == null) {
            adapter =
                chatComponent.chatAdapter(_viewHolderProvider, _imageLoader, _onMessageClick, style)
        }
    }

    fun onStart(
        conversationId: String,
        otherMembers: List<IMemberModel>
    ): ObservableTask<List<ChatItemView>, Throwable> {

        addOnScrollListener(object : PaginationListener(layoutManager as LinearLayoutManager) {
            override fun isLoading(): Boolean =
                chatAdapter.loading

            override fun loadMoreItems(offset: Int) {
                loadMoreMessages(chatAdapter.getItem(offset).sentDate, conversationId, otherMembers)
            }
        })

        return chatComponent.chatClient().subscribeMessages(
            conversationId
        ).onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }.onNext { response ->
            chatComponent.chatClient().setSeenIfNot(
                chatComponent.memberClient().currentUserId(),
                conversationId,
                response.first().id
            )
        }.map { model ->
            ChatItemViewListCreator.create(
                ChatItemViewListConfiguration(
                    chatComponent.currentUserId,
                    model,
                    otherMembers
                )
            )
        }.onNext { itemViews ->
            onMessagesChanged(itemViews)
        }.also { task ->
            disposable.add(task)
        }
    }

    fun onStop() {
        disposable.collect(Disposable::dispose)
    }

    private fun loadMoreMessages(
        startAfter: Date,
        conversationId: String,
        otherMembers: List<IMemberModel>
    ) {
        chatAdapter.loading = true

        chatComponent.chatClient().messages(
            conversationId, startAfter
        ).map { model ->
            ChatItemViewListCreator.create(
                ChatItemViewListConfiguration(
                    chatComponent.currentUserId,
                    model,
                    otherMembers
                )
            )
        }.onSuccess { response ->
            chatAdapter.loading = false
            chatAdapter.submitList(chatAdapter.getItems().plus(response))
        }.onError { error ->
            chatAdapter.loading = false
            logE(error.localizedMessage ?: "Unknown error")
        }
    }

    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter.run {
            submitList(items)

            if (items.isNotEmpty()) {
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }
    }
}

