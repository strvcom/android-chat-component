package com.strv.chat.library.core.ui.conversation.adapter

import com.strv.chat.library.R
import com.strv.chat.library.core.ui.extensions.LayoutId

enum class ConversationViewType(val id: LayoutId) {
    CONVERSATION(R.layout.item_conversation);

    companion object {
        fun viewType(id: LayoutId) = enumValues<ConversationViewType>().firstOrNull {
            it.id == id
        }
    }
}