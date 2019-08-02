package com.strv.chat.library.chat.domain

import com.strv.chat.library.message.domain.Message
import java.util.*

sealed class ChatItem {

    data class ChatHeader(
        val date: Date
    ): ChatItem()

    data class ChatMessage<T: Message>(
        val userId: Long,
        val userName: String,
        val userPhotoUrl: String,
        val message: T
    ): ChatItem()
}

