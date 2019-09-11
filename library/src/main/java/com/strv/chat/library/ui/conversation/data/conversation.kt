package com.strv.chat.library.ui.conversation.data

import java.util.Date

data class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val iconUrl: String,
    val title: String,
    val message: String,
    val sentDate: Date
)