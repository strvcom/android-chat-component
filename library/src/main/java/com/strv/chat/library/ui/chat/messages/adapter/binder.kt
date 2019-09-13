package com.strv.chat.library.ui.chat.messages.adapter

import android.view.ViewGroup

interface ChatItemBinder {

    fun headerBinder(parent: ViewGroup): HeaderViewHolder =
        DefaultHeaderViewHolder(parent)

    fun myMessageBinder(parent: ViewGroup): MyMessageViewHolder =
        DefaultMyMessageViewHolder(parent)

    fun otherMessageBinder(parent: ViewGroup): OtherMessageViewHolder =
        DefaultOtherMessageViewHolder(parent)
}

class DefaultChatItemBinder: ChatItemBinder