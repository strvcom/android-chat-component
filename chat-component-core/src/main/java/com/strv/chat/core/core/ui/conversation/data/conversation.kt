package com.strv.chat.core.core.ui.conversation.data

import com.strv.chat.core.domain.task.Task
import java.util.Date

class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val title: String,
    val pictureTask: Task<String, Throwable>,
    val message: String,
    val sentDate: Date,
    val otherMemberIds: List<String>
)