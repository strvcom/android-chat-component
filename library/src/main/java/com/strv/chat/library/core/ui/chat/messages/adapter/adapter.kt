package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.chat.data.ChatItemView

class ChatAdapter(
    binder: ChatItemBinder
) : ChatItemBinder by binder, RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        when (viewType) {
            R.layout.item_header -> headerBinder(parent)
            R.layout.item_my_message -> myMessageBinder(parent)
            R.layout.item_other_message -> otherMessageBinder(parent)
            R.layout.item_my_image -> myImageBinder(parent)
            R.layout.item_other_image -> otherImageBinder(parent)
            else -> throw IllegalArgumentException("Undefined message type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as ChatItemView.Header)
            is MyMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyTextMessage)
            is OtherMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherTextMessage)
            is MyImageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyImageMessage)
            is OtherImageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherImageMessage)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatItemView.Header -> R.layout.item_header
            is ChatItemView.MyTextMessage -> R.layout.item_my_message
            is ChatItemView.OtherTextMessage -> R.layout.item_other_message
            is ChatItemView.MyImageMessage -> R.layout.item_my_image
            is ChatItemView.OtherImageMessage -> R.layout.item_other_image
        }

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}