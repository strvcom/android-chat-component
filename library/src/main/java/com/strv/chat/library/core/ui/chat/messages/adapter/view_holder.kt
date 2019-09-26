package com.strv.chat.library.core.ui.chat.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.Bindable
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage

abstract class ChatViewHolderBetter<T : ChatItemView>(
    parent: ViewGroup,
    layoutId: Int
) : Bindable<T>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

abstract class HeaderViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolderBetter<Header>(parent, layoutId)

abstract class MyTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolderBetter<MyTextMessage>(parent, layoutId)

abstract class OtherTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolderBetter<OtherTextMessage>(parent, layoutId)

abstract class MyImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolderBetter<MyImageMessage>(parent, layoutId)

abstract class OtherImageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolderBetter<OtherImageMessage>(parent, layoutId)

