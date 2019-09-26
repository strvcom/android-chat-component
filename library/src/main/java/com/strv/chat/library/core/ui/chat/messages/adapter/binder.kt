package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.ViewGroup
import com.strv.chat.library.core.ui.chat.messages.style.ChatRecyclerViewStyle

interface ChatItemBinder {

    fun headerBinder(parent: ViewGroup): HeaderViewHolder =
        DefaultHeaderViewHolder(parent)

    fun myMessageBinder(parent: ViewGroup): MyMessageViewHolder =
        DefaultMyMessageViewHolder(parent)

    fun otherMessageBinder(parent: ViewGroup): OtherMessageViewHolder =
        DefaultOtherMessageViewHolder(parent)

    fun myImageBinder(parent: ViewGroup): MyImageViewHolder =
        DefaultMyImageViewHolder(parent)

    fun otherImageBinder(parent: ViewGroup): OtherImageViewHolder =
        DefaultOtherImageViewHolder(parent)

}

class DefaultChatItemBinder: ChatItemBinder

class StyleableChatItemBinder(
    val style: ChatRecyclerViewStyle
) : ChatItemBinder {

    override fun myMessageBinder(parent: ViewGroup): MyMessageViewHolder {
        return StyleableMyMessageViewHolder(parent, style)
    }

    override fun otherMessageBinder(parent: ViewGroup): OtherMessageViewHolder {
        return StyleableOtherMessageViewHolder(parent, style)
    }
}