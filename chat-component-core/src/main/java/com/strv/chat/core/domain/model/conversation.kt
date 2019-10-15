package com.strv.chat.core.domain.model

import java.util.Date

interface IConversationModel {
    val id: String
    val members: List<IMemberMetaModel>
    val seen: Map<String, ISeenModel>
    val lastMessage: IMessageModel
}

interface ISeenModel {
    val messageId: String?
    val date: Date?
}