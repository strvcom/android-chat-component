package com.strv.chat.library.chat.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.strv.chat.library.chat.domain.ChatItem

//class ChatAdapter(
//    diffCallback: DiffUtil.ItemCallback<ChatItem>
//) : ListAdapter<ChatItem, ChatViewHolder<in ChatItem>>(diffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder<in ChatItem> =
//        ChatHeaderViewHolderImpl(LayoutInflater.from(parent.context).inflate(viewType, parent))
//
//    override fun onBindViewHolder(holder: ChatViewHolder<in ChatItem>, position: Int) {
//        holder.bindItem(getItem(position))
//    }
//
////
////    override fun getItemViewType(position: Int) =
////        when (val chatItem = getItem(position)) {
////            is ChatItem.ChatHeader -> R.layout.item_chat_header
////            is ChatItem.ChatMessage<*> ->
////                when (chatItem.message) {
////                    is Message.TextMessage -> R.layout.item_chat_my_message
////                    else ->
////                }
////        }
//
//}




