package com.strv.chat.library.ui.chat.messages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.Binder
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.imageCircle
import com.strv.chat.library.ui.imageUrl
import com.strv.chat.library.ui.view.RelativeTimeTextView
import com.strv.chat.library.ui.view.TimeTextView

abstract class HeaderViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ChatItemView.Header>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class MyMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ChatItemView.MyTextMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class OtherMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ChatItemView.OtherTextMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class MyImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ChatItemView.MyImageMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class OtherImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<ChatItemView.OtherImageMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

internal class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, R.layout.item_header) {
    private val textDate = itemView.findViewById<RelativeTimeTextView>(R.id.tv_date)

    override fun bind(item: ChatItemView.Header) {
        textDate.date = item.sentDate
    }
}

internal class DefaultMyMessageViewHolder(parent: ViewGroup) :
    MyMessageViewHolder(parent, R.layout.item_my_message) {
    private val textMessage = itemView.findViewById<TextView>(R.id.tv_message)

    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)
    override fun bind(item: ChatItemView.MyTextMessage) {
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}

internal class DefaultOtherMessageViewHolder(parent: ViewGroup) :
    OtherMessageViewHolder(parent, R.layout.item_other_message) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.OtherTextMessage) {
        imageIcon.imageCircle(item.sender.userPhotoUrl)
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}

internal class DefaultMyImageViewHolder(parent: ViewGroup) :
    MyImageViewHolder(parent, R.layout.item_my_image) {

    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.MyImageMessage) {
        image.imageUrl(item.imageUrl)

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}

internal class DefaultOtherImageViewHolder(parent: ViewGroup) :
    OtherImageViewHolder(parent, R.layout.item_other_image) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.OtherImageMessage) {
        imageIcon.imageCircle(item.sender.userPhotoUrl)
        image.imageUrl(item.imageUrl)

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}