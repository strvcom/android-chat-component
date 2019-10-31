package com.strv.chat.core.core.ui.conversation.adapter

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.strv.chat.core.R
import com.strv.chat.core.core.ui.Styleable
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.ON_CLICK_ACTION
import com.strv.chat.core.core.ui.extensions.OnClickAction
import com.strv.chat.core.core.ui.view.TimeTextView
import com.strv.chat.core.domain.IMAGE_LOADER
import com.strv.chat.core.domain.ImageLoader
import strv.ktools.logE

/**
 * Default ViewHolder for [ConversationItemView] type.
 *
 * @property imageIcon [ImageView] that displays the image of a conversation.
 * @property textTitle [TextView] that displays the title of a conversation.
 * @property textLastMessage [TextView] that displays text of the last message of a conversation.
 * @property textDate [TextView] that displays the sent date of the last message of a conversation.
 */
open class DefaultConversationViewHolder(
    parent: ViewGroup
) : ConversationViewHolder(parent, R.layout.item_conversation),
    Styleable<ConversationRecyclerViewStyle> {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_photo)
    private val textTitle = itemView.findViewById<TextView>(R.id.tv_title)
    private val textLastMessage = itemView.findViewById<TextView>(R.id.tv_message)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_date)

    override fun bind(
        item: ConversationItemView,
        imageLoader: ImageLoader?,
        onClickAction: OnClickAction<ConversationItemView>?
    ) {

        imageLoader?.loadAvatar(imageIcon, null) ?: logE("$IMAGE_LOADER is not defined")

        item.pictureTask
            .onSuccess { imageUrl ->
                imageLoader?.loadAvatar(imageIcon, imageUrl) ?: logE("$IMAGE_LOADER is not defined")
            }.onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
            }

        textTitle.text = item.title
        textLastMessage.text = item.message
        textLastMessage.setTypeface(null, if (item.unread) BOLD else NORMAL)
        textDate.date = item.sentDate

        itemView.setOnClickListener {
            onClickAction?.invoke(item) ?: logE("$ON_CLICK_ACTION is not defined")
        }
    }

    override fun applyStyle(style: ConversationRecyclerViewStyle) {
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.titleTextSize.toFloat())
        textTitle.setTextColor(style.titleTextColor)
        textTitle.setTypeface(null, style.titleTextStyle)

        textLastMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.messageTextSize.toFloat())
        textLastMessage.setTextColor(style.messageTextColor)
        textLastMessage.setTypeface(null, style.messageTextStyle)
    }
}