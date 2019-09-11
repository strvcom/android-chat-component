package com.strv.chat.library.ui.conversation

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.ConversationModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.chat.ChatAdapter
import com.strv.chat.library.ui.conversation.data.ConversationItemView
import com.strv.chat.library.ui.conversation.mapper.conversationItemView
import strv.ktools.logE

//todo update!!!!
private val diffUtilCallback = object : DiffUtil.ItemCallback<ConversationItemView>() {

    override fun areItemsTheSame(oldItem: ConversationItemView, newItem: ConversationItemView): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: ConversationItemView, newItem: ConversationItemView): Boolean {
        return true
    }
}

class ConversationRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var conversationAdapter: ConversationAdapter<ViewHolder>?
        get() = super.getAdapter() as ConversationAdapter<ViewHolder>
        private set(value) = super.setAdapter(value)

    private lateinit var conversationClient: ConversationClient
    private lateinit var memberProvider: MemberProvider

    private val conversationsObserver = object : Observer<List<ConversationModel>> {
        override fun onSuccess(response: List<ConversationModel>) {
            onConversationsChanged(conversationItemView(response, memberProvider))
        }

        override fun onError(error: Throwable) {
            logE(error.localizedMessage)
            onConversationsFetchFailed(error)
        }
    }

    operator fun invoke(config: Builder.() -> Unit) {
        Builder().apply(config).build()
    }

    //todo should be fine to add a possibility to define custom Observer<Model> or Observer<View>
    fun startObserving() {
        conversationClient.subscribeConversations(conversationsObserver)
    }

    fun stopObserving() {
        conversationClient.unsubscribeConversations()
    }

    private fun onConversationsChanged(items: List<ConversationItemView>) {
        conversationAdapter?.submitList(items)
    }

    private fun onConversationsFetchFailed(exception: Throwable) {
        Toast.makeText(context, "Error has occured", Toast.LENGTH_SHORT).show()
    }

    inner class Builder(
        var adapter: ConversationAdapter<ViewHolder>? = null,
        var layoutManager: LinearLayoutManager? = null,
        var conversationClient: ConversationClient? = null,
        var memberProvider: MemberProvider? = null
    ) {

        fun build() {
            setAdapter(adapter ?: DefaultConversationAdapter(diffUtilCallback))
            setLayoutManager(layoutManager ?: LinearLayoutManager(context))
            this@ConversationRecyclerView.conversationClient =
                requireNotNull(conversationClient) { "ConversationClient must be specified" }
            this@ConversationRecyclerView.memberProvider =
                requireNotNull(memberProvider) { "MemberProvider must be specified" }
        }
    }
}