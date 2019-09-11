package com.strv.chat.library.domain.model

data class ConversationModel(
    val id: String,
    val members: List<String>,
    val lastMessage: MessageModelResponse
)