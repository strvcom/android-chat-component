package com.strv.chat.library.core.ui.conversation.data.mapper

import com.strv.chat.library.R
import com.strv.chat.library.core.session.ChatComponent.string
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView
import com.strv.chat.library.domain.model.IConversationModel
import com.strv.chat.library.domain.model.IImageMessageModel
import com.strv.chat.library.domain.model.IMessageModel
import com.strv.chat.library.domain.model.ITextMessageModel
import com.strv.chat.library.domain.provider.MemberProvider

internal fun conversationItemView(list: List<IConversationModel>, memberProvider: MemberProvider) =
    list.map { model -> conversationItemView(model, memberProvider) }

private fun conversationItemView(model: IConversationModel, memberProvider: MemberProvider) =
    ConversationItemView(
        model.id,
        false,
        //todo what if there is just one user/more than two users
        model.members
            .filterNot { memberId ->
                memberId != memberProvider.currentUserId()
            }.map { memberId ->
                memberProvider.member(memberId).userPhotoUrl
            }.first(),
        model.members
            .filterNot { memberId ->
                memberId != memberProvider.currentUserId()
            }.fold(listOf<String>()) { acc, id ->
                acc.plus(memberProvider.member(id).userName)
            }.joinToString(),
        lastMessage(model.lastMessage),
        model.lastMessage.sentDate
    )

private fun lastMessage(messageModel: IMessageModel) =
    when (messageModel) {
        is ITextMessageModel -> messageModel.text
        is IImageMessageModel -> string(R.string.user_has_sent_picture)
        else -> throw IllegalArgumentException("Unknown message type")
    }