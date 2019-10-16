package com.strv.chat.core.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction

class ChatAdapter(
    private val chatViewHolderProvider: ChatViewHolderProvider,
    private val onClickAction: OnClickAction<ChatItemView>?,
    private val style: ChatRecyclerViewStyle? = null
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
            is HeaderViewHolder -> holder.bind(getItem(position) as ChatItemView.Header, onClickAction)
            is MyTextMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyTextMessage, onClickAction)
            is OtherTextMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherTextMessage, onClickAction)
            is MyImageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyImageMessage, onClickAction)
            is OtherImageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherImageMessage, onClickAction)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatItemView.Header -> ChatViewType.HEADER.id
            is ChatItemView.MyTextMessage -> ChatViewType.MY_TEXT_MESSAGE.id
            is ChatItemView.OtherTextMessage -> ChatViewType.OTHER_TEXT_MESSAGE.id
            is ChatItemView.MyImageMessage -> ChatViewType.MY_IMAGE_MESSAGE.id
            is ChatItemView.OtherImageMessage -> ChatViewType.OTHER_IMAGE_MESSAGE.id
        }

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItems() =
        differ.currentList

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}