package com.strv.chat.library.core.ui.chat.messages.adapter

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Styleable
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.HEADER
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.MY_IMAGE_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.MY_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.OTHER_IMAGE_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.OTHER_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.imageCenterCropUrl
import com.strv.chat.library.core.ui.extensions.imageCircleUrl
import com.strv.chat.library.core.ui.view.RelativeTimeTextView
import com.strv.chat.library.core.ui.view.TimeTextView

internal class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, HEADER.id) {
    private val textDate = itemView.findViewById<RelativeTimeTextView>(R.id.tv_date)

    override fun bind(item: ChatItemView.Header) {
        textDate.date = item.sentDate
    }
}

internal class DefaultMyMessageViewHolder(parent: ViewGroup) :
    MyTextMessageViewHolder(parent, MY_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

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

    override fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.myTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.myTextMessageTextColor)
    }
}


internal class DefaultOtherMessageViewHolder(parent: ViewGroup) :
    OtherTextMessageViewHolder(parent, OTHER_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

    private val textMessage = itemView.findViewById<TextView>(R.id.tv_message)

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.OtherTextMessage) {
        imageIcon.imageCircleUrl(item.sender.userPhotoUrl)
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }

    override fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.otherTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.otherTextMessageTextColor)
    }
}

internal class DefaultMyImageViewHolder(parent: ViewGroup) :
    MyImageViewHolder(parent, MY_IMAGE_MESSAGE.id) {

    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.Image.MyImageMessage) {
        image.imageCenterCropUrl(item.imageUrl)

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        item.onClick?.run {
            image.setOnClickListener {
                invoke(item)
            }
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}

internal class DefaultOtherImageViewHolder(parent: ViewGroup) :
    OtherImageViewHolder(parent, OTHER_IMAGE_MESSAGE.id) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: ChatItemView.Image.OtherImageMessage) {
        imageIcon.imageCircleUrl(item.sender.userPhotoUrl)
        image.imageCenterCropUrl(item.imageUrl)

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        item.onClick?.run {
            image.setOnClickListener {
                invoke(item)
            }
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}