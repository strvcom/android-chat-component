package com.strv.chat.library.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.ChatObserver
import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.ui.chat.mapper.ChatItemsMapper
import com.strv.chat.library.ui.chat.view.ChatItemView
import com.strv.chat.library.ui.chat.view.MemberView

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

    private lateinit var userId: String
    private lateinit var members: List<MemberView>

    private val messagesObserver = object : ChatObserver {

        override fun onNext(list: List<MessageModel>) {
            onMessagesChanged(ChatItemsMapper.mapToView(userId, members, list))
        }

        override fun onError(error: Throwable) {
            onMessagesFetchFailed(error)
        }
    }

    operator fun invoke(config: Builder.() -> Unit) {
        Builder().apply(config).build()
    }

    fun startObserving(observer: ChatObserver = messagesObserver) {
        chatClient.run {
            subscribeMessages(observer = observer)
        }
    }

    fun stopObserving() {
        chatClient.unsubscribeMessages()
    }

    private fun onMessagesChanged(ChatItemViews: List<ChatItemView>) {
        chatAdapter?.submitList(ChatItemViews)
    }

    private fun onMessagesFetchFailed(exception: Throwable) {
        Toast.makeText(context, "Error has occured", Toast.LENGTH_SHORT).show()
    }

    inner class Builder(
        var userId: String? = null,
        //todo isnt it enough to set the users when the screen is opened?
        var members: List<MemberView>? = null,
        var adapter: ChatAdapter<ViewHolder>? = null,
        var layoutManager: LinearLayoutManager? = null,
        var chatClient: ChatClient? = null
    ) {

        fun build() {
            require(members?.isNotEmpty() == true) { "Chat members can not be empty" }

            setAdapter(adapter ?: DefaultChatAdapter(diffUtilCallback))
            setLayoutManager(layoutManager ?: LinearLayoutManager(context).apply { reverseLayout = true })
            this@ChatRecyclerView.userId = requireNotNull(userId) { "userId must be specified" }
            this@ChatRecyclerView.members = requireNotNull(members) { "otherMembers must be specified" }
            this@ChatRecyclerView.chatClient = requireNotNull(chatClient) { "ChatClient must be specified" }
        }
    }
}

