package com.strv.chat.core.core.ui.conversation.adapter

import com.strv.chat.core.R
import com.strv.chat.core.core.ui.extensions.LayoutId

enum class ConversationViewType(val id: LayoutId) {
    CONVERSATION(R.layout.item_conversation);

    companion object {
        fun viewType(id: LayoutId) = enumValues<ConversationViewType>().firstOrNull {
            it.id == id
        }
    }
}