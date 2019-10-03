package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreConversationEntity

object ConversationModelListCreator :
    Creator<List<IConversationModel>, ConversationModelListConfiguration> {

    override val create: ConversationModelListConfiguration.() -> List<IConversationModel> = {
        conversations.map { conversation ->
            ConversationModelCreator.create(ConversationModelConfiguration(conversation))
        }
    }
}

class ConversationModelListConfiguration(
    val conversations: List<FirestoreConversationEntity>
) : CreatorConfiguration