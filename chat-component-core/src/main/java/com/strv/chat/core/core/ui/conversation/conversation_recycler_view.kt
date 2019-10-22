package com.strv.chat.core.core.ui.conversation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import com.strv.chat.core.core.ui.conversation.adapter.ConversationAdapter
import com.strv.chat.core.core.ui.conversation.adapter.ConversationViewHolderProvider
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.core.ui.conversation.data.creator.ConversationItemViewConfiguration
import com.strv.chat.core.core.ui.conversation.data.creator.ConversationItemViewCreator
import com.strv.chat.core.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.Disposable
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.mapIterable
import com.strv.chat.core.domain.sortedBy
import strv.ktools.logE
import java.util.LinkedList

class ConversationRecyclerView : RecyclerView {

    var viewHolderProvider: ConversationViewHolderProvider
        get() = throw UnsupportedOperationException("")
        set(value) {
            _viewHolderProvider = value
        }

    var imageLoader: ImageLoader
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageLoader = value
        }

    var onConversationClick: OnClickAction<ConversationItemView>
        get() = throw UnsupportedOperationException("")
        set(value) {
            _onConversationClick = value
        }

    private var _viewHolderProvider: ConversationViewHolderProvider =
        chatComponent.conversationViewHolderProvider()

    private var _imageLoader: ImageLoader? = null

    private var _onConversationClick: OnClickAction<ConversationItemView>? = null

    private val disposable = LinkedList<Disposable>()

    private var conversationAdapter: ConversationAdapter?
        get() = super.getAdapter() as ConversationAdapter?
        set(value) = super.setAdapter(value)

    private var style: ConversationRecyclerViewStyle? = null

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

    fun init(builder: ConversationRecyclerView.() -> Unit) {
        builder()

        if (layoutManager == null) {
            layoutManager = LinearLayoutManager(context)
        }

        if (adapter == null) {
            adapter =
                chatComponent.conversationAdapter(
                    _viewHolderProvider,
                    _imageLoader,
                    _onConversationClick,
                    style
                )
        }
    }

    fun onStart() =
        chatComponent.conversationClient().subscribeConversations(
            chatComponent.currentUserId
        ).onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }.mapIterable { model ->
            ConversationItemViewCreator.create(
                ConversationItemViewConfiguration(
                    model,
                    chatComponent.memberClient()
                )
            )
        }.sortedBy { conversation ->
            !conversation.unread
        }.onNext { list ->
            onConversationsChanged(list)
        }.also { task ->
            disposable.add(task)
        }

    fun onStop() {
        disposable.collect(Disposable::dispose)
    }

    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter?.submitList(items)
    }

    private fun init(attrs: AttributeSet? = null) {
        if (attrs != null) {
            style = ConversationRecyclerViewStyle.parse(context, attrs)
        }
    }
}