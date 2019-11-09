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
import com.strv.chat.core.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.HEADER
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.MY_IMAGE_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.MY_TEXT_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.OTHER_IMAGE_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.OTHER_TEXT_MESSAGE
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.ON_CLICK_ACTION
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.core.ui.view.RelativeDateTextView
import com.strv.chat.core.core.ui.view.TimeTextView
import com.strv.chat.core.domain.IMAGE_LOADER
import com.strv.chat.core.domain.ImageLoader
import strv.ktools.logE

/**
 * Default ViewHolder for [Header] type.
 *
 * @property textDate [TextView] that displays the sent date of a message.
 */
open class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, HEADER.id) {

    protected val textDate = itemView.findViewById<RelativeDateTextView>(R.id.tv_date)

    override fun bind(
        item: Header,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<Header>?
    ) {
        textDate.date = item.sentDate
    }
}

/**
 * Default ViewHolder for [MyTextMessage] type.
 *
 * @property textMessage [TextView] that displays the text of a message.
 * @property textDate [TextView] that displays the sent date of a message.
 */
open class DefaultMyTextMessageViewHolder(parent: ViewGroup) :
    MyTextMessageViewHolder(parent, MY_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: MyTextMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<MyTextMessage>?
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

/**
 * Default ViewHolder for [OtherTextMessage] type.
 *
 * @property textMessage [TextView] that displays the text of a message.
 * @property imageIcon [ImageView] that displays the image of the sender.
 * @property textDate [TextView] that displays the sent date of a message.
 */
open class DefaultOtherTextMessageViewHolder(parent: ViewGroup) :
    OtherTextMessageViewHolder(parent, OTHER_TEXT_MESSAGE.id), Styleable<ChatRecyclerViewStyle> {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    protected val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: OtherTextMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<OtherTextMessage>?
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

/**
 * Default ViewHolder for [MyImageMessage] type.
 *
 * @property image [ImageView] that displays an image message.
 * @property textDate [TextView] that displays the sent date of a message.
 */
open class DefaultMyImageMessageViewHolder(parent: ViewGroup) :
    MyImageMessageViewHolder(parent, MY_IMAGE_MESSAGE.id) {

    protected val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: MyImageMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<MyImageMessage>?
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

/**
 * Default ViewHolder for [OtherImageMessage] type.
 *
 * @property imageIcon [ImageView] that displays the image of the sender.
 * @property image [ImageView] that displays an image message.
 * @property textDate [TextView] that displays the sent date of a message.
 */
open class DefaultOtherImageMessageViewHolder(parent: ViewGroup) :
    OtherImageMessageViewHolder(parent, OTHER_IMAGE_MESSAGE.id) {

    protected val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    protected val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    protected val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(
        item: OtherImageMessage,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<OtherImageMessage>?
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

/**
 * ViewHolder for progress item view.
 */
internal class ProgressViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        ChatViewType.PROGRESS.id, parent, false
    )
)