package com.strv.chat.library.ui.conversation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.conversation.data.ConversationItemView
import com.strv.chat.library.ui.conversation.mapper.conversationItemView

class ConversationRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var conversationAdapter: ConversationAdapter
        get() = super.getAdapter() as ConversationAdapter
        private set(value) = super.setAdapter(value)

    private lateinit var conversationClient: ConversationClient
    private lateinit var memberProvider: MemberProvider

    operator fun invoke(config: Builder.() -> Unit) {
        Builder().apply(config).build()
    }

    fun startObserving(observer: Observer<List<ConversationItemView>>) {
        conversationClient.subscribeConversations(observer.convert { response ->
            conversationItemView(response, memberProvider).also(::onConversationsChanged)
        })
    }

    fun stopObserving() {
        conversationClient.unsubscribeConversations()
    }

    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter.submitList(items)
    }

    inner class Builder(
        var adapter: ConversationAdapter? = null,
        var layoutManager: LinearLayoutManager? = null,
        var conversationClient: ConversationClient? = null,
        var memberProvider: MemberProvider? = null
    ) {

        fun build() {
            setLayoutManager(layoutManager ?: LinearLayoutManager(context))
            setAdapter(requireNotNull(adapter) { "ConversationAdapter must be specified" })
            this@ConversationRecyclerView.conversationClient =
                requireNotNull(conversationClient) { "ConversationClient must be specified" }
            this@ConversationRecyclerView.memberProvider =
                requireNotNull(memberProvider) { "MemberProvider must be specified" }
        }
    }
}