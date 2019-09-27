package com.strv.chat.library.firestore.model

import com.strv.chat.library.domain.model.IConversationModel
import com.strv.chat.library.domain.model.IMessageModel

internal data class FirestoreConversationModel(
    override val id: String,
    override val members: List<String>,
    override val lastMessage: IMessageModel
): IConversationModel