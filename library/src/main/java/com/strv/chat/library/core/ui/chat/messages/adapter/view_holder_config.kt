package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Styleable
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.MY_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.OTHER_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle

typealias ViewHolderConstructor<T> = (ViewGroup, LayoutId) -> ChatViewHolderBetter<T>

class ChatViewHolders(
    private val headerConfig: ViewHolderConstructor<Header> =
        { parent, _ -> DefaultHeaderViewHolder(parent) },
    private val myTextMessageConfig: ViewHolderConstructor<MyTextMessage> =
        { parent, _ -> DefaultMyMessageViewHolder(parent) },
    private val otherTextMessageConfig: ViewHolderConstructor<OtherTextMessage> =
        { parent, _ -> DefaultOtherMessageViewHolder(parent) },
    private val myImageMessageConfig: ViewHolderConstructor<MyImageMessage> =
        { parent, _ -> DefaultMyImageViewHolder(parent) },
    private val otherImageMessageConfig: ViewHolderConstructor<OtherImageMessage> =
        { parent, _ -> DefaultOtherImageViewHolder(parent) }
) {

    internal fun holder(
        parent: ViewGroup,
        viewType: Int,
        style: ChatRecyclerViewStyle?
    ): RecyclerView.ViewHolder =
        when (ChatViewType.viewType(viewType)) {
            ChatViewType.HEADER -> {
                headerConfig(parent, viewType).also { holder ->
                    checkStyle(holder, style)
                }
            }
            MY_TEXT_MESSAGE -> {
                myTextMessageConfig(parent, viewType).also { holder ->
                    checkStyle(holder, style)
                }
            }
            OTHER_TEXT_MESSAGE -> {
                otherTextMessageConfig(parent, viewType).also { holder ->
                    checkStyle(holder, style)
                }
            }
            ChatViewType.MY_IMAGE_MESSAGE -> {
                myImageMessageConfig(parent, viewType).also { holder ->
                    checkStyle(holder, style)
                }
            }
            ChatViewType.OTHER_IMAGE_MESSAGE -> {
                otherImageMessageConfig(parent, viewType).also { holder ->
                    checkStyle(holder, style)
                }
            }
            null -> throw IllegalStateException("Wrong message view type.")
        }

    internal fun bind(holder: RecyclerView.ViewHolder, item: ChatItemView) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as Header)
            is MyTextMessageViewHolder -> holder.bind(item as MyTextMessage)
            is OtherTextMessageViewHolder -> holder.bind(item as OtherTextMessage)
            is MyImageViewHolder -> holder.bind(item as MyImageMessage)
            is OtherImageViewHolder -> holder.bind(item as OtherImageMessage)
        }
    }

    internal fun itemViewType(item: ChatItemView): Int =
        when (item) {
            is Header -> R.layout.item_header
            is MyTextMessage -> MY_TEXT_MESSAGE.id
            is OtherTextMessage -> OTHER_TEXT_MESSAGE.id
            is MyImageMessage -> R.layout.item_my_image
            is OtherImageMessage -> R.layout.item_other_image
        }

    @Suppress("UNCHECKED_CAST")
    private fun checkStyle(holder: ChatViewHolderBetter<*>, style: ChatRecyclerViewStyle?) {
        if (style != null && holder is Styleable<*>) (holder as Styleable<ChatRecyclerViewStyle>).applyStyle(
            style
        )
    }
}

