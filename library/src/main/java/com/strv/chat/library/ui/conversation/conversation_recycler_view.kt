package com.strv.chat.library.ui.conversation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.OnClickAction
import com.strv.chat.library.ui.conversation.adapter.ConversationAdapter
import com.strv.chat.library.ui.conversation.adapter.ConversationBinder
import com.strv.chat.library.ui.conversation.adapter.DefaultConversationBinder
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

    operator fun invoke(
        conversationClient: ConversationClient,
        memberProvider: MemberProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(conversationClient, memberProvider).apply(config).build()
    }

    fun startObserving(observer: Observer<List<ConversationItemView>>, onItemClick: OnClickAction<ConversationItemView>) {
        conversationClient.subscribeConversations(
            memberProvider.currentUserId(),
            observer.convert { response ->
                conversationItemView(response, memberProvider, onItemClick).also(::onConversationsChanged)
            })
    }

    fun stopObserving() {
        conversationClient.unsubscribeConversations()
    }

    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter.submitList(items)
    }

    inner class Builder(
        val conversationClient: ConversationClient,
        val memberProvider: MemberProvider,
        var binder: ConversationBinder? = null,
        var layoutManager: LinearLayoutManager? = null
    ) {

        fun build() {
            this@ConversationRecyclerView.conversationClient = conversationClient
            this@ConversationRecyclerView.memberProvider = memberProvider
            //todo consider using component root provider
            adapter = ConversationAdapter(binder ?: DefaultConversationBinder())
            setLayoutManager(layoutManager ?: LinearLayoutManager(context))
        }
    }
}