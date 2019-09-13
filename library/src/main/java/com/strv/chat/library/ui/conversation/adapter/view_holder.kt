package com.strv.chat.library.ui.conversation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.Binder
import com.strv.chat.library.ui.conversation.data.ConversationItemView
import com.strv.chat.library.ui.imageCircle
import com.strv.chat.library.ui.view.TimeTextView

abstract class ConversationViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ConversationItemView>, RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

internal class DefaultConversationViewHolder(
    parent: ViewGroup
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
            item.onClick(item)
        }
    }
}