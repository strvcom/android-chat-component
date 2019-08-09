package com.strv.chat.library.data.model

import java.util.*

sealed class Message(open val sendDate: Date, open val senderId: Long){

    data class TextMessage(
        override val sendDate: Date,
        override val senderId: Long,
        val text: String
    ) : Message(sendDate, senderId)

}

