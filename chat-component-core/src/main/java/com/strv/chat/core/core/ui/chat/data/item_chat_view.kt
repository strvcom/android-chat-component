package com.strv.chat.core.core.ui.chat.data

import com.strv.chat.core.core.ui.data.MemberView
import java.util.Date

// Header messages doesn't have any DB generated id
private const val HEADER_ID = "HEADER_ID"

/**
 * Represents UI structure of a message.
 *
 * @property id Message id.
 * @property sentDate Date when the message was sent.
 */
sealed class ChatItemView(open val id: String, open val sentDate: Date) {

    /**
     * Represents UI structure of a header.
     *
     * @constructor Creates a new header item view.
     */
    data class Header(
        override val sentDate: Date
    ) : ChatItemView(HEADER_ID, sentDate)

    /**
     * Represents UI structure of an outgoing text message.
     *
     * @param text Text of the message.
     * @param showSentDate Displays [sentDate] if true otherwise not.
     *
     * @constructor Creates a new outgoing text message item view.
     */
    data class MyTextMessage(
        override val id: String,
        override val sentDate: Date,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    /**
     * Represents UI structure of an incoming text message.
     *
     * @param sender Sender of the message
     * @param text Text of the message.
     * @param showSentDate Displays [sentDate] if true otherwise not.
     *
     * @constructor Creates a new incoming text message item view.
     */
    data class OtherTextMessage(
        override val id: String,
        override val sentDate: Date,
        val sender: MemberView,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    /**
     * Represents UI structure of an outgoing image message.
     *
     * @param imageUrl Url of the image contained in the image message.
     * @param showSentDate Displays [sentDate] if true otherwise not.
     *
     * @constructor Creates a new outgoing image message item view.
     */
    data class MyImageMessage(
        override val id: String,
        override val sentDate: Date,
        val imageUrl: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    /**
     * Represents UI structure of an incoming image message.
     *
     * @param sender Sender of the message
     * @param imageUrl Url of the image contained in the image message.
     * @param showSentDate Displays [sentDate] if true otherwise not.
     *
     * @constructor Creates a new incoming image message item view.
     */
    data class OtherImageMessage(
        override val id: String,
        override val sentDate: Date,
        val imageUrl: String,
        val sender: MemberView,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}