package com.strv.chat.core.core.ui.conversation.data

import com.strv.chat.core.domain.task.Task
import java.util.Date

/**
 * Represents UI structure of a conversation.
 *
 * @property id Conversation id.
 * @property unread Is true if the last message of the conversation was read by the current user, false otherwise.
 * @property title Title of the conversation. For one-to-one conversation the title is equal to the name of the counterparty user.
 * @property pictureTask Wrapper around an asynchronous call that returns conversation image url, which for one-to-one conversation is equal to the image url of the counterparty user, in case of success, [Throwable] in case of error.
 * @property message Text representing the content of the last message of the conversation.
 * @property sentDate Date when the last message was sent.
 * @property otherMemberIds List of members of the conversation excluding the current user id.
 */
class ConversationItemView(
    val id: String,
    val unread: Boolean,
    val title: String,
    val pictureTask: Task<String, Throwable>,
    val message: String,
    val sentDate: Date,
    val otherMemberIds: List<String>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConversationItemView

        if (id != other.id) return false
        if (unread != other.unread) return false
        if (title != other.title) return false
        if (message != other.message) return false
        if (sentDate != other.sentDate) return false
        if (otherMemberIds != other.otherMemberIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + unread.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + sentDate.hashCode()
        result = 31 * result + otherMemberIds.hashCode()
        return result
    }
}