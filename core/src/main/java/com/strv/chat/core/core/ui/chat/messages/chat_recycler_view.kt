package com.strv.chat.core.core.ui.chat.messages

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnLayoutChangeListener
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
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task.Disposable
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.map
import strv.ktools.logE
import java.util.Date
import java.util.LinkedList

/**
 * Customizable component that displays messages of a selected conversation and listens for realtime updates.
 */
class ChatRecyclerView : RecyclerView {

    /**
     * [ChatViewHolderProvider] setter.
     */
    var viewHolderProvider: ChatViewHolderProvider
        get() = throw UnsupportedOperationException("")
        set(value) {
            _viewHolderProvider = value
        }

    /**
     * Sets an action that is performed when the user clicks on a message.
     */
    var onItemClick: OnClickAction<ChatItemView>
        get() = throw UnsupportedOperationException("")
        set(value) {
            _onItemClick = value
        }

    /**
     * [ImageLoader] setter.
     */
    var imageLoader: ImageLoader
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageLoader = value
        }

    /**
     * Allows to add custom implementations of [RecyclerView.ViewHolder].
     */
    private var _viewHolderProvider: ChatViewHolderProvider? = null

    /**
     * Action that is performed after the user clicks on a conversation.
     */
    private var _onItemClick: OnClickAction<ChatItemView>? = null

    /**
     * Defines a way how to upload picture's urls to ImageViews.
     */
    private var _imageLoader: ImageLoader? = null

    /**
     * List of disposable entities.
     */
    private val disposable = LinkedList<Disposable>()

    /**
     * @see [RecyclerView.Adapter].
     */
    private var chatAdapter: ChatAdapter
        get() = super.getAdapter() as ChatAdapter
        private set(value) = super.setAdapter(value)

    /**
     * Ensures that the layout scrolls down when the first bunch of messages is loaded.
     */
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

    /**
     * Ensures that the layout adapts when visibility of a soft keyboard changes.
     */
    private val onLayoutChangeListener =
        OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom && chatAdapter.itemCount.compareTo(0) == 1) {
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }

    /**
     * Style of the view.
     */
    private var style: ChatRecyclerViewStyle? = null

    //constructors
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    /**
     * Type-safe builder that allows creating Kotlin-based domain-specific languages (DSLs) suitable for configuring [ChatRecyclerView] in a semi-declarative way.
     */
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
                chatComponent.chatAdapter(_viewHolderProvider ?: chatComponent.chatViewHolderProvider(), _imageLoader, _onItemClick, style)
        }
    }

    /**
     * Starts listening to the message source.
     *
     * A recommended way is to start listening in onStart() method of your activity/fragment or as soon as you receive [conversationId] and [otherMembers] list.
     *
     * @param conversationId Id of the superior conversation.
     * @param otherMembers Other members of the conversation.
     *
     * @return [ObservableTask] that emits a List<[ChatItemView]> in case of success, an error otherwise
     */
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

    /**
     * Stops listening to the message source and cleans unused resources.
     */
    fun onStop() {
        disposable.collect(Disposable::dispose)
    }

    /**
     * Implements paging functionality.
     *
     * Listening for realtime changes is not supported for additional messages.
     */
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

    /**
     * It is called when the messages, the component is listening to, change.
     */
    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter.run {
            submitList(items)

            if (items.isNotEmpty()) {
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }
    }

    /**
     * Initialization of the component called from its constructor.
     */
    private fun init(attrs: AttributeSet? = null) {
        addOnLayoutChangeListener(onFirstLayoutChangeListener)

        if (attrs != null) {
            style = ChatRecyclerViewStyle.parse(context, attrs)
        }
    }
}

