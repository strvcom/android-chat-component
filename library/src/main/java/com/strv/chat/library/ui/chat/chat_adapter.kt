package com.strv.chat.library.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.domain.model.ChatItem
import com.strv.chat.library.domain.model.Message

abstract class ChatAdapter<VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    abstract fun submitList(list: List<ChatItem>)

    abstract fun getItem(position: Int): ChatItem
}

class DefaultChatAdapter(diffCallback: DiffUtil.ItemCallback<ChatItem>): ChatAdapter<DefaultChatViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun submitList(list: List<ChatItem>) {
        differ.submitList(list)
    }

    override fun getItem(position: Int): ChatItem =
        differ.currentList[position]

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultChatViewHolder =
        DefaultChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    override fun onBindViewHolder(holder: DefaultChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DefaultChatViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val messageText = view.findViewById<TextView>(R.id.tv_message)

    fun bind(item: ChatItem) {
        if (item is ChatItem.ChatMessage<*> && item.message is Message.TextMessage) {
            messageText.text = item.message.text
        } else {
            messageText.text = "Error"
        }
    }
}