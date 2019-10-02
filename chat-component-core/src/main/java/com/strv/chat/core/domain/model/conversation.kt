package com.strv.chat.core.domain.model

interface IConversationModel {
    val id: String
    val members: List<String>
    val lastMessage: IMessageModel
}