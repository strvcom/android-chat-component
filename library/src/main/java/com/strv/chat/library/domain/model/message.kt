package com.strv.chat.library.domain.model

import java.util.*

sealed class MessageModel(id: String, sentDate: Date, senderId: String) {

    data class TextMessageModel(
        val id: String,
        val sentDate: Date,
        val senderId: String,
        val text: String
    ) : MessageModel(id, sentDate, senderId)

    data class ImageMessageModel(
        val id: String,
        val sentDate: Date,
        val senderId: String,
        val image: Image
    ) : MessageModel(id, sentDate, senderId) {

        data class Image(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}
