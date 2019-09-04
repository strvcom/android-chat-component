package com.strv.chat.library.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.domain.model.ChatItem
import com.strv.chat.library.domain.model.Message

private val diffUtilCallback = object: DiffUtil.ItemCallback<ChatItem>() {
    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return true
    }
}

class ChatAdapter: ListAdapter<ChatItem, ChatViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder =
        ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChatViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val messageText = view.findViewById<TextView>(R.id.tv_message)

    fun bind(item: ChatItem) {
        if (item is ChatItem.ChatMessage<*> && item.message is Message.TextMessage) {
            messageText.text = item.message.text
        } else {
            messageText.text = "Error"
        }
    }
}