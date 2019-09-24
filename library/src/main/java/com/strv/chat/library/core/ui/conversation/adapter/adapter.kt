package com.strv.chat.library.core.ui.conversation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView

class ConversationAdapter(
    conversationBinder: ConversationBinder
) : ConversationBinder by conversationBinder, RecyclerView.Adapter<ConversationViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ConversationItemView>() {

        override fun areItemsTheSame(
            oldItem: ConversationItemView,
            newItem: ConversationItemView
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConversationItemView,
            newItem: ConversationItemView
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ by lazy { AsyncListDiffer(this, diffUtilCallback) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        conversationBinder(parent)

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<ConversationItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ConversationItemView =
        differ.currentList[position]
}