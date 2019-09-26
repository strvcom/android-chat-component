package com.strv.chat.library.core.ui.conversation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.session.ChatComponent
import com.strv.chat.library.core.session.ChatComponent.conversationAdapter
import com.strv.chat.library.core.session.ChatComponent.conversationViewHolderProvider
import com.strv.chat.library.core.ui.conversation.adapter.ConversationAdapter
import com.strv.chat.library.core.ui.conversation.adapter.ConversationViewHolderProvider
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView
import com.strv.chat.library.core.ui.conversation.data.mapper.conversationItemView
import com.strv.chat.library.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.OnClickAction
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.provider.MemberProvider
import strv.ktools.logE
import java.util.*

class ConversationRecyclerView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr){

    private val disposable = LinkedList<Disposable>()

    private var conversationAdapter: ConversationAdapter
        get() = super.getAdapter() as ConversationAdapter
        private set(value) = super.setAdapter(value)

    private lateinit var memberProvider: MemberProvider

    private var style: ConversationRecyclerViewStyle? = null

    init {
        if (attrs != null) {
            style = ConversationRecyclerViewStyle.parse(context, attrs)
        }
    }

    fun init(
        memberProvider: MemberProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(memberProvider).apply(config).build()

        if (attrs != null) {
            style = ConversationRecyclerViewStyle.parse(context, attrs)
        }
    }

    fun onStart(onItemClick: OnClickAction<ConversationItemView>) =
        ChatComponent.conversationClient().subscribeConversations(
            memberProvider.currentUserId()
        ).onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }.onNext { model ->
            conversationItemView(model, memberProvider, onItemClick).also(::onConversationsChanged)
        }.also { task ->
            disposable.add(task)
        }


    fun onStop() {
        while (disposable.isNotEmpty()) {
            disposable.pop().dispose()
        }
    }

    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter.submitList(items)
    }

    inner class Builder(
        val memberProvider: MemberProvider,
        var viewHolderProvider: ConversationViewHolderProvider = conversationViewHolderProvider(),
        var layoutManager: LinearLayoutManager? = null
    ) {

        fun build() {
            this@ConversationRecyclerView.memberProvider = memberProvider
            adapter = conversationAdapter(viewHolderProvider, style)
            setLayoutManager(layoutManager ?: LinearLayoutManager(context))
        }
    }
}