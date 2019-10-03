package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMessageEntity

object MessageModelListCreator : Creator<List<IMessageModel>, MessageModelListConfiguration> {

    override val create: MessageModelListConfiguration.() -> List<IMessageModel> = {
        messages.map { message ->
            MessageModelCreator.create(MessageModelConfiguration(message))
        }
    }
}

class MessageModelListConfiguration(
    val messages: List<FirestoreMessageEntity>
) : CreatorConfiguration