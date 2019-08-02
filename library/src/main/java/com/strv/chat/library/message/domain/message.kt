package com.strv.chat.library.message.domain

import java.util.*

interface Message {
    val sendDate: Date
    val senderId: Long
}

sealed class MessageType() : Message {

    data class TextMessage(
        override val sendDate: Date,
        override val senderId: Long,
        val text: String
    ) : MessageType()

}


