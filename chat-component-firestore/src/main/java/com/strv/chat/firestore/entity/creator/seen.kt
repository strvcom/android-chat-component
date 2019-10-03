package com.strv.chat.firestore.entity.creator

import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreSeen

object SeenEntityCreator : Creator<FirestoreSeen, SeenEntityConfiguration> {

    override val create: SeenEntityConfiguration.() -> FirestoreSeen = {
        FirestoreSeen(message.id)
    }
}

class SeenEntityConfiguration(
    val message: IMessageModel
): CreatorConfiguration