package com.strv.chat.core.core.ui.conversation.data

import com.strv.chat.core.core.ui.data.MemberView
import com.strv.chat.core.domain.Task
import java.util.Date

class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val membersTask: Task<List<MemberView>, Throwable>,
    val message: String,
    val sentDate: Date
)