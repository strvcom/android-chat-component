package com.strv.chat.core.core.ui.chat.messages.adapter

import com.strv.chat.core.R
import com.strv.chat.core.core.ui.extensions.LayoutId

enum class ChatViewType(val id: LayoutId) {
    HEADER(R.layout.item_header),
    MY_TEXT_MESSAGE(R.layout.item_my_message),
    OTHER_TEXT_MESSAGE(R.layout.item_other_message),
    MY_IMAGE_MESSAGE(R.layout.item_my_image),
    OTHER_IMAGE_MESSAGE(R.layout.item_other_image);

    companion object {
        fun viewType(id: LayoutId) = enumValues<ChatViewType>().firstOrNull {
            it.id == id
        }
    }
}