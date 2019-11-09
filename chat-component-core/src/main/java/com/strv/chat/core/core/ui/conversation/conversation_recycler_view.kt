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
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.task.Disposable
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.mapIterable
import com.strv.chat.core.domain.task.sortedBy
import strv.ktools.logE
import java.util.LinkedList

/**
 * Customizable component that displays conversations of a user and listens for realtime updates.
 */
class ConversationRecyclerView : RecyclerView {

    /**
     * [ConversationViewHolderProvider] setter.
     */
    var viewHolderProvider: ConversationViewHolderProvider
        get() = throw UnsupportedOperationException("")
        set(value) {
            _viewHolderProvider = value
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
     * Setter for an action that is performed after the user clicks on a conversation.
     */
    var onItemClick: OnClickAction<ConversationItemView>
        get() = throw UnsupportedOperationException("")
        set(value) {
            _onItemClick = value
        }

    /**
     * Allows to add custom implementations of [RecyclerView.ViewHolder].
     */
    private var _viewHolderProvider: ConversationViewHolderProvider =
        chatComponent.conversationViewHolderProvider()

    /**
     * Defines a way how to upload picture's urls to ImageViews.
     */
    private var _imageLoader: ImageLoader? = null

    /**
     * Action that is performed after the user clicks on a conversation.
     */
    private var _onItemClick: OnClickAction<ConversationItemView>? = null

    /**
     * List of disposable entities.
     */
    private val disposable = LinkedList<Disposable>()

    private var conversationAdapter: ConversationAdapter?
        get() = super.getAdapter() as ConversationAdapter?
        set(value) = super.setAdapter(value)

    /**
     * Style of the view.
     */
    private var style: ConversationRecyclerViewStyle? = null

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
     * Type-safe builder that allows creating Kotlin-based domain-specific languages (DSLs) suitable for configuring [ConversationRecyclerView] in a semi-declarative way.
     */
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
                    _onItemClick,
                    style
                )
        }
    }

    /**
     * Starts listening to the conversation source.
     * 
     * A recommended way is to start listening in onStart() method of your activity/fragment.
     *
     * @return [ObservableTask] that emits List<[ConversationItemView]> in case of success, an error otherwise.
     */
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

    /**
     * Stops listening to the conversation source and cleans unused resources.
     */
    fun onStop() {
        disposable.collect(Disposable::dispose)
    }

    /**
     * It is called when the conversation list changes.
     */
    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter?.submitList(items)
    }

    /**
     * Initialization of the component called from its constructor.
     */
    private fun init(attrs: AttributeSet? = null) {
        if (attrs != null) {
            style = ConversationRecyclerViewStyle.parse(context, attrs)
        }
    }
}