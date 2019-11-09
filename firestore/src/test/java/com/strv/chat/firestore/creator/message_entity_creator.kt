package com.strv.chat.firestore.creator

import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.creator.MessageEntityConfiguration
import com.strv.chat.firestore.entity.creator.MessageEntityCreator
import org.junit.Assert.assertEquals
import org.junit.Test

class MessageEntityCreatorTest {

    @Test
    fun `create Entity object from ITextMessageModel`() {
        val id = "BBsNsWNLfNFAEe9kKjpJ"

        val messageInputModel = MessageInputModel.TextInputModel(
            "user-1",
            "conversationId",
            "Hello the first message"
        )

        val messageEntity =
            FirestoreMessageEntity(
                "BBsNsWNLfNFAEe9kKjpJ",
                "user-1",
                "text",
                FirestoreDataEntity("Hello the first message")
            )

        assertEquals(
            messageEntity,
            MessageEntityCreator.create(MessageEntityConfiguration(id, messageInputModel))
        )
    }

    @Test
    fun `create Entity object from IImageMessageModel`() {
        val id = "CCsNsWNffNFAE39kKjpJ"

        val messageInputModel = MessageInputModel.ImageInputModel(
            "user-2",
            "conversationId",
            MessageInputModel.ImageInputModel.ImageModel("image_url_is_here")
        )

        val messageEntity =
            FirestoreMessageEntity(
                "CCsNsWNffNFAE39kKjpJ",
                "user-2",
                "image",
                FirestoreDataEntity(image = FirestoreImageDataEntity("image_url_is_here"))
            )

        assertEquals(
            messageEntity,
            MessageEntityCreator.create(MessageEntityConfiguration(id, messageInputModel))
        )
    }
}

