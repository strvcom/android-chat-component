package com.strv.chat.library.chat.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.chat.domain.ChatItem
import com.strv.chat.library.chat.domain.ChatItem.ChatHeader
import com.strv.chat.library.message.domain.Message

abstract class ChatViewHolder<T : ChatItem>(
    parent: ViewGroup,
    layoutId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutId, parent)
) {

    abstract fun bindItem(chatItem: T)
}

//abstract class ChatHeaderViewHolder(
//    itemView: View
//) : ChatViewHolder<ChatHeader>(itemView)
//
//class ChatHeaderViewHolderImpl(
//    itemView: View
//) : ChatHeaderViewHolder(itemView) {
//
//    private val textRelativeTime = itemView.findViewById<TextView>(R.id.tv_relative_time)
//
//    override fun bindItem(chatItem: ChatHeader) {
//        textRelativeTime.text = chatItem.date.toString()
//    }
//}
//
//abstract class ChatMessageViewHolder<T : Message>(
//    itemView: View
//) : ChatViewHolder<ChatItem.ChatMessage<T>>(itemView)

//class TextMessageViewHolder(view: View) : MessageViewHolder<TextMessage>(view) {
//
//    val messageTextView = view.findViewById<TextView>(R.id.tv_message)
//
//    val dateTextView = view.findViewById<TextView>(R.id.tv_message_date)
//
//    override fun bindItem(chatItem: TextMessage) {
//        messageTextView.text = chatItem.text
//        dateTextView.text = chatItem.sendDate.toString()
//    }
//}
