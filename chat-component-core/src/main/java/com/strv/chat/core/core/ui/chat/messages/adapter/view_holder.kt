package com.strv.chat.core.core.ui.chat.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.Bindable
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.core.core.ui.chat.data.ChatItemView.OtherTextMessage

/**
 * General ViewHolder for [ChatItemView] types.
 *
 * @param layoutId ID for an XML layout resource to load.
 * @param parent View to be the parent of the generated hierarchy.
 */
abstract class ChatViewHolder<T : ChatItemView>(
    parent: ViewGroup,
    layoutId: Int
) : Bindable<T>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

/**
 * ViewHolder for [Header] type.
 */
abstract class HeaderViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<Header>(parent, layoutId)

/**
 * ViewHolder for [MyTextMessage] type.
 */
abstract class MyTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<MyTextMessage>(parent, layoutId)

/**
 * ViewHolder for [OtherTextMessage] type.
 */
abstract class OtherTextMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<OtherTextMessage>(parent, layoutId)

/**
 * ViewHolder for [MyImageMessage] type.
 */
abstract class MyImageMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<MyImageMessage>(parent, layoutId)

/**
 * ViewHolder for [OtherImageMessage] type.
 */
abstract class OtherImageMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : ChatViewHolder<OtherImageMessage>(parent, layoutId)

