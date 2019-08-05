package com.strv.chat.library.chat.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.business.ChatClient
import com.strv.chat.library.chat.domain.ChatItem
import java.lang.Exception

class ChatRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ChatClient.Listener {

    //not good
    private var chatClient: ChatClient<Any>? = null
    set(value) {
        field = value
        //when to unregistrate listener?
        field?.registerListener(this)
    }

    override fun onMessagesFetchFailed(exception: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val chatAdapter: ListAdapter<ChatItem, ViewHolder>?
    get() = adapter as ListAdapter<ChatItem, ViewHolder>

    init {
        View.inflate(context, R.layout.chat_recycler_view, this)
    }

    override fun onMessagesChanged(chatItems: List<ChatItem>) {
        chatAdapter?.submitList(chatItems)
    }







}