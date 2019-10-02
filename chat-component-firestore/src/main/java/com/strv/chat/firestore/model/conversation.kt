package com.strv.chat.firestore.model

import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IMessageModel

internal data class FirestoreConversationModel(
    override val id: String,
    override val members: List<String>,
    override val lastMessage: IMessageModel
): IConversationModel