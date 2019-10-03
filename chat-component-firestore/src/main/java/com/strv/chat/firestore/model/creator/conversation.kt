package com.strv.chat.firestore.model.creator

import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.entity.LAST_MESSAGE
import com.strv.chat.firestore.entity.MEMBERS
import com.strv.chat.firestore.model.FirestoreConversationModel
import strv.ktools.logE

object ConversationModelCreator : Creator<IConversationModel, ConversationModelConfiguration> {

    override val create: ConversationModelConfiguration.() -> IConversationModel = {
        FirestoreConversationModel(
            requireNotNull(conversation.id) { "$ID must me specified" },
            requireNotNull(conversation.members) { logE("$MEMBERS must be specified") }.also { members ->
                require(members.isNotEmpty()) { logE("$MEMBERS can not be empty") }
            },
            MessageModelCreator.create(MessageModelConfiguration(requireNotNull(conversation.lastMessage) {
                "$LAST_MESSAGE must be specified"
            }))
        )
    }
}

class ConversationModelConfiguration(
    val conversation: FirestoreConversationEntity
) : CreatorConfiguration