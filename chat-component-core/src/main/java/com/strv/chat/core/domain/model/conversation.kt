package com.strv.chat.core.domain.model

import java.util.Date

interface IConversationModel {
    val id: String
    val members: List<String>
    val seen: Map<String, ISeenModel>
    val lastMessage: IMessageModel
}

interface ISeenModel {
    val messageId: String?
    val date: Date?
}