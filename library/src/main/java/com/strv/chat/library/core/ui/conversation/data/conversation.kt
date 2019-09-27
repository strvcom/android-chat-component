package com.strv.chat.library.core.ui.conversation.data

import java.util.*

class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val iconUrl: String,
    val title: String,
    val message: String,
    val sentDate: Date
)