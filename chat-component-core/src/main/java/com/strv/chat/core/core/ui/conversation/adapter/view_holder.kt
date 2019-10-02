package com.strv.chat.core.core.ui.conversation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.Bindable
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView

abstract class ConversationViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : Bindable<ConversationItemView>,
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
