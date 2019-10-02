package com.strv.chat.core.core.ui.chat.data

import com.strv.chat.core.core.ui.data.MemberView
import java.util.Date

const val HEADER_ID = "NO_ID"

sealed class ChatItemView(open val id: String, open val sentDate: Date) {

    data class Header(
        override val sentDate: Date
    ) : ChatItemView(HEADER_ID, sentDate)

    data class MyTextMessage(
        override val id: String,
        override val sentDate: Date,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    data class OtherTextMessage(
        override val id: String,
        override val sentDate: Date,
        val sender: MemberView,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)


    data class MyImageMessage(
        override val id: String,
        override val sentDate: Date,
        val imageUrl: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

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