package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle

class ChatAdapter(
    private val chatViewHolders: ChatViewHolders,
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
        chatViewHolders.holder(parent, viewType, style)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        chatViewHolders.bind(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int =
        chatViewHolders.itemViewType(getItem(position))

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItems() =
        differ.currentList

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}