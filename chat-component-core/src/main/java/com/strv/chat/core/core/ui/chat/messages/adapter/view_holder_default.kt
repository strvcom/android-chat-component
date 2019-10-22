package com.strv.chat.core.core.ui.chat.messages.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.R
import com.strv.chat.core.core.ui.Styleable
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.HEADER
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.MY_IMAGE_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.MY_TEXT_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.OTHER_IMAGE_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.OTHER_TEXT_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.ON_CLICK_ACTION
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.core.ui.view.RelativeTimeTextView
import com.strv.chat.core.core.ui.view.TimeTextView
import com.strv.chat.core.domain.IMAGE_LOADER
import com.strv.chat.core.domain.ImageLoader
import strv.ktools.logE

open class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, HEADER.id) {

    protected val textDate = itemView.findViewById<RelativeTimeTextView>(R.id.tv_date)

    override fun bind(
        item: ChatItemView.Header,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView.Header>?
    ) {
        textDate.date = item.sentDate
    }
}

open class DefaultMyMessageViewHolder(parent: ViewGroup) :
    MyTextMessageViewHolder(parent, MY_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: ChatItemView.MyTextMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView.MyTextMessage>?
    ) {
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate), imageLoader, onClickAction)
        }
    }

    override fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.myTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.myTextMessageTextColor)
    }
}


open class DefaultOtherMessageViewHolder(parent: ViewGroup) :
    OtherTextMessageViewHolder(parent, OTHER_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    protected val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: ChatItemView.OtherTextMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView.OtherTextMessage>?
    ) {
        imageLoader?.loadAvatar(imageIcon, item.sender.userPhotoUrl)
            ?: logE("$IMAGE_LOADER is not defined")
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate), imageLoader, onClickAction)
        }
    }

    override fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.otherTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.otherTextMessageTextColor)
    }
}

open class DefaultMyImageViewHolder(parent: ViewGroup) :
    MyImageViewHolder(parent, MY_IMAGE_MESSAGE.id) {

    protected val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: ChatItemView.MyImageMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView.MyImageMessage>?
    ) {
        imageLoader?.loadImageMessage(image, item.imageUrl) ?: logE("$IMAGE_LOADER is not defined")

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        image.setOnClickListener {
            onClickAction?.invoke(item) ?: logE("$ON_CLICK_ACTION is not defined")
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate), imageLoader, onClickAction)
        }
    }
}

open class DefaultOtherImageViewHolder(parent: ViewGroup) :
    OtherImageViewHolder(parent, OTHER_IMAGE_MESSAGE.id) {

    protected val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    protected val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: ChatItemView.OtherImageMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ChatItemView.OtherImageMessage>?
    ) {
        imageLoader?.loadAvatar(imageIcon, item.sender.userPhotoUrl)
            ?: logE("$IMAGE_LOADER is not defined")

        imageLoader?.loadImageMessage(image, item.imageUrl) ?: logE("$IMAGE_LOADER is not defined")

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        image.setOnClickListener {
            onClickAction?.invoke(item) ?: logE("$ON_CLICK_ACTION is not defined")
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate), imageLoader, onClickAction)
        }
    }
}

internal class ProgressViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        ChatViewType.PROGRESS.id, parent, false
    )
)