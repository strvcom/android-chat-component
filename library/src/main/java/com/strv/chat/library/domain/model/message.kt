package com.strv.chat.library.domain.model

import java.util.*

sealed class Message(open val sendDate: Date){

    data class TextMessage(
        override val sendDate: Date,
        val text: String
    ) : Message(sendDate)

}
