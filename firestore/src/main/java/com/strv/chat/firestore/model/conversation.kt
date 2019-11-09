package com.strv.chat.firestore.model

import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.ISeenModel
import java.util.Date

internal data class FirestoreConversationModel(
    override val id: String,
    override val members: List<IMemberMetaModel>,
    override val seen: Map<String, ISeenModel?>,
    override val lastMessage: IMessageModel
): IConversationModel

internal data class FirestoreSeenModel(
    override val messageId: String,
    override val date: Date
): ISeenModel