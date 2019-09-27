package com.strv.chat.library.domain.model

sealed class MessageInputModel(
    open val senderId: String,
    open val conversationId: String
) {
    data class TextInputModel(
        override val senderId: String,
        override val conversationId: String,
        val text: String
    ) : MessageInputModel(senderId, conversationId)

    data class ImageInputModel(
        override val senderId: String,
        override val conversationId: String,
        val imageModel: ImageModel
    ) : MessageInputModel(senderId, conversationId) {

        data class ImageModel(
            val width: Double,
            val height: Double,
            val original: String
        )
    }
}