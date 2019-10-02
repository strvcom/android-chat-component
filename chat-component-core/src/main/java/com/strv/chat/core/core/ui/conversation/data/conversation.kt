package com.strv.chat.core.core.ui.conversation.data

import java.util.Date

class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val iconUrl: String,
    val title: String,
    val message: String,
    val sentDate: Date
)