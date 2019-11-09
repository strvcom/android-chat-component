package com.strv.chat.core.core.ui.chat.messages.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.Styleable
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.core.core.ui.chat.messages.adapter.ChatViewType.*
import com.strv.chat.core.core.ui.chat.messages.style.ChatRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.LayoutId

typealias ChatViewHolderConstructor<T> = (ViewGroup, LayoutId) -> ChatViewHolder<T>

/**
 * Holds all possible ViewHolder configurations and allows to add a custom implementations of ViewHolder.
 *
 * In case that one property is not filled, the default implementation will be used.
 *
 * @property headerConfig [HeaderViewHolder] configuration.
 * @property myTextMessageConfig [MyTextMessageViewHolder] configuration.
 * @property otherTextMessageConfig [OtherImageMessageViewHolder] configuration.
 * @property myImageMessageConfig [MyImageMessageViewHolder] configuration.
 * @property otherImageMessageConfig [OtherImageMessageViewHolder] configuration.
*/
class ChatViewHolderProvider(
    private val headerConfig: ChatVHConfig<Header> =
        ChatVHConfig(HEADER.id) { parent, _ -> DefaultHeaderViewHolder(parent) },
    private val myTextMessageConfig: ChatVHConfig<MyTextMessage> =
        ChatVHConfig(MY_TEXT_MESSAGE.id) { parent, _ -> DefaultMyTextMessageViewHolder(parent) },
    private val otherTextMessageConfig: ChatVHConfig<OtherTextMessage> =
        ChatVHConfig(OTHER_TEXT_MESSAGE.id) { parent, _ ->
            DefaultOtherTextMessageViewHolder(
                parent
            )
        },
    private val myImageMessageConfig: ChatVHConfig<MyImageMessage> =
        ChatVHConfig(MY_IMAGE_MESSAGE.id) { parent, _ -> DefaultMyImageMessageViewHolder(parent) },
    private val otherImageMessageConfig: ChatVHConfig<OtherImageMessage> =
        ChatVHConfig(OTHER_IMAGE_MESSAGE.id) { parent, _ ->
            DefaultOtherImageMessageViewHolder(
                parent
            )
        }
) {

    /**
     * Returns a [ChatViewHolder] based on [viewType]
     */
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
            PROGRESS -> {
                ProgressViewHolder(parent)
            }
            null -> throw IllegalStateException("Wrong message view type.")
        }

    /**
     * Applies style to [Styleable].
     */
    @Suppress("UNCHECKED_CAST")
    private fun checkStyle(holder: ChatViewHolder<*>, style: ChatRecyclerViewStyle?) {
        if (style != null && holder is Styleable<*>) (holder as Styleable<ChatRecyclerViewStyle>).applyStyle(
            style
        )
    }
}

/**
 * Container holding configuration for creating a [ChatViewHolder].
 *
 * @param layoutId layout resource id.
 * @param constructor function that returns a new [ChatViewHolder] object.
 *
 * @constructor Creates a new configuration for creating a [ChatViewHolder].
 */
class ChatVHConfig<T : ChatItemView>(
    layoutId: LayoutId,
    constructor: ChatViewHolderConstructor<T>
) {

    /**
     * Creates a new [ChatViewHolder] object.
     *
     * @return [ChatViewHolder]
     */
    val viewHolder: (ViewGroup) -> ChatViewHolder<T> = { group ->
        constructor(group, layoutId)
    }
}

