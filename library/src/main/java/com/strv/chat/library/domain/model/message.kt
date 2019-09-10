package com.strv.chat.library.domain.model

import java.util.Date

sealed class MessageModel(open val sentDate: Date, open val senderId: String) {

    data class TextMessageModel(
        override val sentDate: Date,
        override val senderId: String,
        val text: String
    ) : MessageModel(sentDate, senderId)

    data class ImageMessageModel(
        override val sentDate: Date,
        override val senderId: String,
        val image: Image
    ) : MessageModel(sentDate, senderId) {

        data class Image(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}
