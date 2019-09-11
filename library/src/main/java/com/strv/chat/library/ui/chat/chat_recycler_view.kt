package com.strv.chat.library.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.chat.mapper.chatItemView

//todo update!!!!
private val diffUtilCallback = object : DiffUtil.ItemCallback<ChatItemView>() {

    override fun areItemsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
        return true
    }
}

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var chatAdapter: ChatAdapter<ViewHolder>?
        get() = super.getAdapter() as ChatAdapter<ViewHolder>
        private set(value) = super.setAdapter(value)

    private lateinit var chatClient: ChatClient
    private lateinit var memberProvider: MemberProvider

    //todo where is the best place to handle seen property?
    private val messagesObserver = object : Observer<List<MessageModelResponse>> {
        override fun onSuccess(response: List<MessageModelResponse>) {
            chatClient.setSeen(memberProvider.currentUserId(), response.first())

            onMessagesChanged(chatItemView(response, memberProvider))
        }

        override fun onError(error: Throwable) {
            onMessagesFetchFailed(error)
        }
    }

    init {
        addOnLayoutChangeListener { v, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom && chatAdapter?.itemCount?.compareTo(0) == 1) {
                postDelayed({ scrollToPosition(0) }, 50)
            }
        }
    }

    operator fun invoke(config: Builder.() -> Unit) {
        Builder().apply(config).build()
    }

    fun startObserving() {
        chatClient.subscribeMessages(messagesObserver)
    }

    fun stopObserving() {
        chatClient.unsubscribeMessages()
    }

    private fun onMessagesChanged(items: List<ChatItemView>) {
        chatAdapter?.run {
            submitList(items)
            if (items.isNotEmpty()) smoothScrollToPosition(0)
        }
    }

    private fun onMessagesFetchFailed(exception: Throwable) {
        Toast.makeText(context, "Error has occured", Toast.LENGTH_SHORT).show()
    }

    inner class Builder(
        var adapter: ChatAdapter<ViewHolder>? = null,
        var layoutManager: LinearLayoutManager? = null,
        var chatClient: ChatClient? = null,
        var memberProvider: MemberProvider? = null
    ) {

        fun build() {
            setAdapter(adapter ?: DefaultChatAdapter(diffUtilCallback))
            setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
                setClipToPadding(false)
            })
            this@ChatRecyclerView.chatClient =
                requireNotNull(chatClient) { "ChatClient must be specified" }
            this@ChatRecyclerView.memberProvider =
                requireNotNull(memberProvider) { "MemberProvider must be specified" }
        }
    }
}

