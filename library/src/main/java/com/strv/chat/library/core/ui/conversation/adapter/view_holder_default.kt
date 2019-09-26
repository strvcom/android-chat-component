package com.strv.chat.library.core.ui.conversation.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Styleable
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView
import com.strv.chat.library.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.imageCircleUrl
import com.strv.chat.library.core.ui.view.TimeTextView

internal class DefaultConversationViewHolder(
    parent: ViewGroup
) : ConversationViewHolder(parent, R.layout.item_conversation), Styleable<ConversationRecyclerViewStyle> {
    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val textLastMessage = itemView.findViewById<TextView>(R.id.tv_message)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_date)

    override fun bind(item: ConversationItemView) {
        imageIcon.imageCircleUrl(item.iconUrl)
        textUserName.text = item.title
        textLastMessage.text = item.message
        textDate.date = item.sentDate

        itemView.setOnClickListener {
            item.onClick(item)
        }
    }

    override fun applyStyle(style: ConversationRecyclerViewStyle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}