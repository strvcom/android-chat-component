package com.strv.chat.firestore.creator

import com.google.firebase.Timestamp
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.model.FirestoreImageMessageModel
import com.strv.chat.firestore.model.FirestoreImageModel
import com.strv.chat.firestore.model.FirestoreTextMessageModel
import com.strv.chat.firestore.model.creator.MessageModelConfiguration
import com.strv.chat.firestore.model.creator.MessageModelCreator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class MessageModelCreatorTest {

    @Test
    fun `create ITextMessageModel object from Entity object`() {
        val messageEntity =
            FirestoreMessageEntity(
                "BBsNsWNLfNFAEe9kKjpJ",
                "user-1",
                "text",
                FirestoreDataEntity("Hello the first message"),
                Timestamp(1572873536, 583000000)
            )

        val messageModel = FirestoreTextMessageModel(
            "BBsNsWNLfNFAEe9kKjpJ",
            Date(1572873536583),
            "user-1",
            "Hello the first message"
        )

        assertEquals(
            messageModel,
            MessageModelCreator.create(MessageModelConfiguration(messageEntity))
        )
    }

    @Test
    fun `create IImageMessageModel object from Entity object`() {
        val messageEntity =
            FirestoreMessageEntity(
                "CCsNsWNffNFAE39kKjpJ",
                "user-2",
                "image",
                FirestoreDataEntity(image = FirestoreImageDataEntity("image_url_is_here")),
                Timestamp(1572873538, 583332000)
            )

        val messageModel = FirestoreImageMessageModel(
            "CCsNsWNffNFAE39kKjpJ",
            Date(1572873538583),
            "user-2",
            FirestoreImageModel("image_url_is_here")
        )

        assertEquals(
            messageModel,
            MessageModelCreator.create(MessageModelConfiguration(messageEntity))
        )
    }
}

