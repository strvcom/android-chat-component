package com.strv.chat.library.domain.model

import java.util.Date

sealed class MessageModelRequest(open val senderId: String) {

    data class TextMessageModel(
        override val senderId: String,
        val text: String
    ) : MessageModelRequest(senderId)

    data class ImageMessageModel(
        override val senderId: String,
        val image: Image
    ) : MessageModelRequest(senderId) {

        data class Image(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}

sealed class MessageModelResponse(
    open val id: String,
    open val sentDate: Date,
    open val senderId: String
) {

    data class TextMessageModel(
        override val id: String,
        override val sentDate: Date,
        override val senderId: String,
        val text: String
    ) : MessageModelResponse(id, sentDate, senderId)

    data class ImageMessageModel(
        override val id: String,
        override val sentDate: Date,
        override val senderId: String,
        val image: Image
    ) : MessageModelResponse(id, sentDate, senderId) {

        data class Image(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}
