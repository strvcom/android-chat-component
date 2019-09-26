package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.Styleable
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.HEADER
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.MY_IMAGE_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.MY_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.OTHER_IMAGE_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.adapter.ChatViewType.OTHER_TEXT_MESSAGE
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.LayoutId

typealias ChatViewHolderConstructor<T> = (ViewGroup, LayoutId) -> ChatViewHolder<T>

class ChatViewHolderProvider(
    private val headerConfig: ChatVHConfig<Header> =
        ChatVHConfig(HEADER.id) { parent, _ -> DefaultHeaderViewHolder(parent) },
    private val myTextMessageConfig: ChatVHConfig<MyTextMessage> =
        ChatVHConfig(MY_TEXT_MESSAGE.id) { parent, _ -> DefaultMyMessageViewHolder(parent) },
    private val otherTextMessageConfig: ChatVHConfig<OtherTextMessage> =
        ChatVHConfig(OTHER_TEXT_MESSAGE.id) { parent, _ ->
            DefaultOtherMessageViewHolder(
                parent
            )
        },
    private val myImageMessageConfig: ChatVHConfig<MyImageMessage> =
        ChatVHConfig(MY_IMAGE_MESSAGE.id) { parent, _ -> DefaultMyImageViewHolder(parent) },
    private val otherImageMessageConfig: ChatVHConfig<OtherImageMessage> =
        ChatVHConfig(OTHER_IMAGE_MESSAGE.id) { parent, _ ->
            DefaultOtherImageViewHolder(
                parent
            )
        }
) {

    internal fun holder(
        parent: ViewGroup,
        viewType: Int,
        style: ChatRecyclerViewStyle?
    ): RecyclerView.ViewHolder =
        when (ChatViewType.viewType(viewType)) {
            HEADER -> {
                headerConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            MY_TEXT_MESSAGE -> {
                myTextMessageConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            OTHER_TEXT_MESSAGE -> {
                otherTextMessageConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            MY_IMAGE_MESSAGE -> {
                myImageMessageConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            OTHER_IMAGE_MESSAGE -> {
                otherImageMessageConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            null -> throw IllegalStateException("Wrong message view type.")
        }

    @Suppress("UNCHECKED_CAST")
    private fun checkStyle(holder: ChatViewHolder<*>, style: ChatRecyclerViewStyle?) {
        if (style != null && holder is Styleable<*>) (holder as Styleable<ChatRecyclerViewStyle>).applyStyle(
            style
        )
    }
}

class ChatVHConfig<T : ChatItemView>(
    layoutId: LayoutId,
    constructor: ChatViewHolderConstructor<T>
) {

    val viewHolder: (ViewGroup) -> ChatViewHolder<T> = { group ->
        constructor(group, layoutId)
    }
}

