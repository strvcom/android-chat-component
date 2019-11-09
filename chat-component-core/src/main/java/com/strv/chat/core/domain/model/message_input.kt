package com.strv.chat.core.domain.model

/**
 * Represents a structure of a message to be sent.
 */
sealed class MessageInputModel(

    /**
     * Id of the sender of the message.
     */
    open val senderId: String,

    /**
     * Id of the superior conversation.
     */
    open val conversationId: String
) {

    /**
     * Represents a structure of a text message to be sent.
     */
    data class TextInputModel(
        override val senderId: String,
        override val conversationId: String,

        /**
         * Text of the message.
         */
        val text: String
    ) : MessageInputModel(senderId, conversationId)

    /**
     * Represents a structure of an image message to be sent.
     */
    data class ImageInputModel(
        override val senderId: String,
        override val conversationId: String,

        /**
         * Image data.
         */
        val imageModel: ImageModel
    ) : MessageInputModel(senderId, conversationId) {

        /**
         * Represents a structure of image data.
         */
        data class ImageModel(

            /**
             * Url of the image.
             */
            val url: String
        )
    }
}