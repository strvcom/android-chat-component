package com.strv.chat.library.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.chat.data.ChatItemView

class ChatAdapter(
    binder: ChatItemBinder
) : ChatItemBinder by binder, RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val diffUtilCallback = object : DiffUtil.ItemCallback<ChatItemView>() {

        //todo is it enought?
        override fun areItemsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
            return oldItem.sentDate == newItem.sentDate
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
            else -> throw IllegalArgumentException("Undefined message type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as ChatItemView.Header)
            is MyMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.MyTextMessage)
            is OtherMessageViewHolder -> holder.bind(getItem(position) as ChatItemView.OtherTextMessage)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatItemView.Header -> R.layout.item_header
            is ChatItemView.MyTextMessage -> R.layout.item_my_message
            is ChatItemView.OtherTextMessage -> R.layout.item_other_message
        }

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}