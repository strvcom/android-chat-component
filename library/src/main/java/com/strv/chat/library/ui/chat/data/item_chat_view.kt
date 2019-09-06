package com.strv.chat.library.ui.chat.data

import java.util.*

sealed class ChatItemView(sentDate: Date) {

    data class ChatHeader(
        val sentDate: Date
    ): ChatItemView(sentDate)

    data class MyTextMessage(
        val id: String,
        val sentDate: Date,
        val text: String,
        val showSentDate: Boolean = false
    ): ChatItemView(sentDate)

    data class OtherTextMessage(
        val id: String,
        val sentDate: Date,
        val sender: MemberView,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(sentDate)
}

data class MemberView(
    val userId: String,
    val userName: String,
    val userPhotoUrl: String
)