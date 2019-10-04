package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.ISeenModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreSeenEntity
import com.strv.chat.firestore.model.FirestoreSeenModel

object SeenModelCreator : Creator<ISeenModel, SeenModelConfiguration> {

    override val create: SeenModelConfiguration.() -> ISeenModel = {
        FirestoreSeenModel(
            seen.messageId,
            seen.timestamp?.toDate()
        )
    }
}

class SeenModelConfiguration(
    val seen: FirestoreSeenEntity
) : CreatorConfiguration