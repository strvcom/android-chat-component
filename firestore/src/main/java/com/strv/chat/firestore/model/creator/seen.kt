package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.ISeenModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreSeenEntity
import com.strv.chat.firestore.entity.MESSAGE_ID
import com.strv.chat.firestore.entity.TIMESTAMP
import com.strv.chat.firestore.model.FirestoreSeenModel

internal object SeenModelCreator : Creator<ISeenModel, SeenModelConfiguration> {

    override val create: SeenModelConfiguration.() -> ISeenModel = {
        FirestoreSeenModel(
            requireNotNull(seen.messageId) { "$MESSAGE_ID must be specified" },
            requireNotNull(seen.timestamp?.toDate()) { "$TIMESTAMP must be specified" }
        )
    }
}

internal class SeenModelConfiguration(
    val seen: FirestoreSeenEntity
) : CreatorConfiguration