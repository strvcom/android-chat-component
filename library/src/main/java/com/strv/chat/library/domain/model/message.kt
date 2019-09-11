package com.strv.chat.library.domain.model

import java.util.Date

sealed class MessageModel(sentDate: Date, senderId: String) {

    data class TextMessageModel(
        val sentDate: Date,
        val senderId: String,
        val text: String
    ) : MessageModel(sentDate, senderId)

    data class ImageMessageModel(
        val sentDate: Date,
        val senderId: String,
        val image: Image
    ) : MessageModel(sentDate, senderId) {

        data class Image(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}
