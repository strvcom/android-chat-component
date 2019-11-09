package com.strv.chat.firestore.creator

import com.google.firebase.Timestamp
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.model.FirestoreImageMessageModel
import com.strv.chat.firestore.model.FirestoreImageModel
import com.strv.chat.firestore.model.FirestoreTextMessageModel
import com.strv.chat.firestore.model.creator.MessageModelListConfiguration
import com.strv.chat.firestore.model.creator.MessageModelListCreator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class MessageModelListCreatorTest {

    @Test
    fun `create IMessageModel List from Entity List`() {
        val messageEntityList = listOf(
            FirestoreMessageEntity(
                "BBsNsWNLfNFAEe9kKjpJ",
                "user-1",
                "text",
                FirestoreDataEntity("Hello the first message"),
                Timestamp(1572873536, 583000000)
            ),
            FirestoreMessageEntity(
                "CCsNsWNffNFAE39kKjpJ",
                "user-2",
                "image",
                FirestoreDataEntity(image = FirestoreImageDataEntity("image_url_is_here")),
                Timestamp(1572873538, 583332000)
            )
        )

        val messageModelList = listOf(
            FirestoreTextMessageModel(
                "BBsNsWNLfNFAEe9kKjpJ",
                Date(1572873536583),
                "user-1",
                "Hello the first message"
            ), FirestoreImageMessageModel(
                "CCsNsWNffNFAE39kKjpJ",
                Date(1572873538583),
                "user-2",
                FirestoreImageModel("image_url_is_here")
            )
        )

        assertEquals(
            messageModelList,
            MessageModelListCreator.create(MessageModelListConfiguration(messageEntityList))
        )
    }
}

