package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.Bindable
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage

abstract class ChatViewHolder<T : ChatItemView>(
    parent: ViewGroup,
    layoutId: Int
) : Bindable<T>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class HeaderViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<Header>(parent, layoutId)

abstract class MyTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<MyTextMessage>(parent, layoutId)

abstract class OtherTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<OtherTextMessage>(parent, layoutId)

abstract class MyImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<MyImageMessage>(parent, layoutId)

abstract class OtherImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<OtherImageMessage>(parent, layoutId)

