package com.strv.chat.library.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.conversation.data.ConversationItemView
import com.strv.chat.library.ui.imageCircle
import com.strv.chat.library.ui.view.TimeTextView

typealias OnClickAction = (String) -> Unit

open class ConversationAdapter(
    private val onItemClick: OnClickAction
) : RecyclerView.Adapter<ConversationViewHolder>() {

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
        conversationViewHolder(parent)

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    open fun conversationViewHolder(viewGroup: ViewGroup): ConversationViewHolder =
        DefaultConversationViewHolder(viewGroup, onItemClick)

    fun submitList(list: List<ConversationItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ConversationItemView =
        differ.currentList[position]
}

abstract class ConversationViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun bind(item: ConversationItemView)
}

internal class DefaultConversationViewHolder(
    parent: ViewGroup,
    private val onClick: OnClickAction
) : ConversationViewHolder(parent, R.layout.item_conversation) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val textLastMessage = itemView.findViewById<TextView>(R.id.tv_message)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_date)

    override fun bind(item: ConversationItemView) {
        imageIcon.imageCircle(item.iconUrl)
        textUserName.text = item.title
        textLastMessage.text = item.message
        textDate.date = item.sentDate

        itemView.setOnClickListener {
            onClick(item.id)
        }
    }
}