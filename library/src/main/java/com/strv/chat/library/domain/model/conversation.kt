package com.strv.chat.library.domain.model

interface IConversationModel {
    val id: String
    val members: List<String>
    val lastMessage: IMessageModel
}