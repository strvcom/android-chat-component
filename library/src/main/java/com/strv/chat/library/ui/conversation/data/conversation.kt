package com.strv.chat.library.ui.conversation.data

import com.strv.chat.library.ui.OnClickAction
import java.util.Date

class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val iconUrl: String,
    val title: String,
    val message: String,
    val sentDate: Date,
    val onClick: OnClickAction<ConversationItemView>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConversationItemView

        if (id != other.id) return false
        if (unread != other.unread) return false
        if (iconUrl != other.iconUrl) return false
        if (title != other.title) return false
        if (message != other.message) return false
        if (sentDate != other.sentDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + unread.hashCode()
        result = 31 * result + iconUrl.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + sentDate.hashCode()
        return result
    }
}