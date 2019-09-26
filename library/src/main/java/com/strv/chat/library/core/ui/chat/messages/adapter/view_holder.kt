package com.strv.chat.library.core.ui.chat.messages.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Binder
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.OtherImageMessage
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.imageCircleUrl
import com.strv.chat.library.core.ui.extensions.imageCenterCropUrl
import com.strv.chat.library.core.ui.view.RelativeTimeTextView
import com.strv.chat.library.core.ui.view.TimeTextView

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
) : Binder<MyImageMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class OtherImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Binder<OtherImageMessage>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

internal class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, R.layout.item_header) {
    private val textDate = itemView.findViewById<RelativeTimeTextView>(R.id.tv_date)

    override fun bind(item: ChatItemView.Header) {
        textDate.date = item.sentDate
    }
}

internal open class DefaultMyMessageViewHolder(parent: ViewGroup) :
    MyMessageViewHolder(parent, R.layout.item_my_message) {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)

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

internal class StyleableMyMessageViewHolder(
    parent: ViewGroup,
    private val style: ChatRecyclerViewStyle
) : DefaultMyMessageViewHolder(parent) {

    override fun bind(item: ChatItemView.MyTextMessage) {
        applyStyle(style)
        super.bind(item)
    }

    private fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.myTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.myTextMessageTextColor)
    }
}

internal open class DefaultOtherMessageViewHolder(parent: ViewGroup) :
    OtherMessageViewHolder(parent, R.layout.item_other_message) {

    protected val textMessage = itemView.findViewById<TextView>(R.id.tv_message)

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
}

internal class StyleableOtherMessageViewHolder(
    parent: ViewGroup,
    private val style: ChatRecyclerViewStyle
) : DefaultOtherMessageViewHolder(parent) {

    override fun bind(item: ChatItemView.OtherTextMessage) {
        super.bind(item)
        applyStyle(style)
    }

    private fun applyStyle(style: ChatRecyclerViewStyle) {
        textMessage.background = style.otherTextMessageBackgroundDrawable
        textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.textMessageTextSize.toFloat())
        textMessage.setTextColor(style.otherTextMessageTextColor)
    }
}

internal class DefaultMyImageViewHolder(parent: ViewGroup) :
    MyImageViewHolder(parent, R.layout.item_my_image) {

    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: MyImageMessage) {
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
    OtherImageViewHolder(parent, R.layout.item_other_image) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val image = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: OtherImageMessage) {
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