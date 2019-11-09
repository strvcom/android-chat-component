package com.strv.chat.core.core.ui.conversation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.conversation.adapter.ConversationViewType.CONVERSATION
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.domain.ImageLoader

internal class ConversationAdapter(
    private val conversationViewHolderProvider: ConversationViewHolderProvider,
    private val imageLoader: ImageLoader? = null,
    private val onConversationClick: OnClickAction<ConversationItemView>? = null,
    private val style: ConversationRecyclerViewStyle?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        conversationViewHolderProvider.holder(parent, viewType, style)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConversationViewHolder -> holder.bind(
                getItem(position),
                imageLoader,
                onConversationClick
            )
        }
    }

    override fun getItemViewType(position: Int): Int =
        CONVERSATION.id

    override fun getItemCount(): Int =
        differ.currentList.size

    /**
     * Pass a new list to the AsyncListDiffer. Adapter updates will be computed on a background
     * thread.
     *
     * @param newList The new List.
     */
    fun submitList(list: List<ConversationItemView>) {
        differ.submitList(list)
    }

    /**
     * Get an item based on index.
     *
     * @param position position of the item in the list.
     *
     * @return item with index [position].
     */
    fun getItem(position: Int): ConversationItemView =
        differ.currentList[position]
}