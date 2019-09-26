package com.strv.chat.library.core.ui.conversation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.Styleable
import com.strv.chat.library.core.ui.conversation.adapter.ConversationViewType.CONVERSATION
import com.strv.chat.library.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.library.core.ui.extensions.LayoutId

typealias ConversationVHConstructor = (ViewGroup, LayoutId) -> ConversationViewHolder

class ConversationViewHolderProvider(
    private val conversationVHConfig: ConversationVHConfig =
        ConversationVHConfig(CONVERSATION.id) { parent, _ ->
            DefaultConversationViewHolder(
                parent
            )
        }
) {

    internal fun holder(
        parent: ViewGroup,
        viewType: Int,
        style: ConversationRecyclerViewStyle?
    ): RecyclerView.ViewHolder =
        when (ConversationViewType.viewType(viewType)) {
            CONVERSATION -> {
                conversationVHConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            null -> throw IllegalStateException("Wrong message view type.")
        }

    @Suppress("UNCHECKED_CAST")
    private fun checkStyle(holder: ConversationViewHolder, style: ConversationRecyclerViewStyle?) {
        if (style != null && holder is Styleable<*>) {
            (holder as Styleable<ConversationRecyclerViewStyle>).applyStyle(style)
        }
    }
}

class ConversationVHConfig(
    layoutId: LayoutId,
    constructor: ConversationVHConstructor
) {

    val viewHolder: (ViewGroup) -> ConversationViewHolder = { group ->
        constructor(group, layoutId)
    }
}