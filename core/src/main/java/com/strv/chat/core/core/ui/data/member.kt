package com.strv.chat.core.core.ui.data

/**
 * Represents UI structure of a member of a conversation.
 *
 * @property userName Name of the member.
 * @property userPhotoUrl Image url of the member.
 */
data class MemberView(
    val userName: String,
    val userPhotoUrl: String
)