package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle

class ChatAdapter(
    private val chatViewHolderProvider: ChatViewHolderProvider,
    private val style: ChatRecyclerViewStyle?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ChatItemView>() {

        override fun areItemsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
            return oldItem == newItem
        }
    }

    private val differ by lazy { AsyncListDiffer(this, diffUtilCallback) }

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        chatViewHolderProvider.holder(parent, viewType, style)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as ChatItemView.Header)
            is MyTextMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyTextMessage)
            is OtherTextMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherTextMessage)
            is MyImageViewHolder -> holder.bind(getItem(position) as ChatItemView.Image.MyImageMessage)
            is OtherImageViewHolder -> holder.bind(getItem(position) as ChatItemView.Image.OtherImageMessage)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatItemView.Header -> ChatViewType.HEADER.id
            is ChatItemView.MyTextMessage -> ChatViewType.MY_TEXT_MESSAGE.id
            is ChatItemView.OtherTextMessage -> ChatViewType.OTHER_TEXT_MESSAGE.id
            is ChatItemView.Image.MyImageMessage -> ChatViewType.MY_IMAGE_MESSAGE.id
            is ChatItemView.Image.OtherImageMessage -> ChatViewType.OTHER_IMAGE_MESSAGE.id
        }

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItems() =
        differ.currentList

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}