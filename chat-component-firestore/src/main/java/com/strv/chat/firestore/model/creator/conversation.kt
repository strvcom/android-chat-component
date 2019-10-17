package com.strv.chat.firestore.model.creator

import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.entity.LAST_MESSAGE
import com.strv.chat.firestore.entity.MEMBERS_META
import com.strv.chat.firestore.entity.SEEN
import com.strv.chat.firestore.model.FirestoreConversationModel
import strv.ktools.logE

object ConversationModelCreator : Creator<IConversationModel, ConversationModelConfiguration> {

    override val create: ConversationModelConfiguration.() -> IConversationModel = {
        FirestoreConversationModel(
            requireNotNull(conversation.id) { "$ID must me specified" },
            MemberMetaModelsCreator.create(MemberMetaModelsConfiguration(  requireNotNull(conversation.membersMeta) { logE("$MEMBERS_META must be specified") })),
            requireNotNull(conversation.seen?.mapValues { entry ->
                entry.value?.let { value -> SeenModelCreator.create(SeenModelConfiguration(value)) }
            }) { "$SEEN must be specified" },
            MessageModelCreator.create(MessageModelConfiguration(requireNotNull(conversation.lastMessage) {
                "$LAST_MESSAGE must be specified"
            }))
        )
    }
}

class ConversationModelConfiguration(
    val conversation: FirestoreConversationEntity
) : CreatorConfiguration