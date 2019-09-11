package com.strv.chat.library.ui.conversation.mapper

import com.strv.chat.library.domain.model.ConversationModel
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.conversation.data.ConversationItemView

internal fun conversationItemView(list: List<ConversationModel>, memberProvider: MemberProvider) =
    list.map { model -> conversationItemView(model, memberProvider) }

private fun conversationItemView(model: ConversationModel, memberProvider: MemberProvider) =
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

private fun lastMessage(messageModel: MessageModelResponse) =
    when (messageModel) {
        is MessageModelResponse.TextMessageModel -> messageModel.text
        //todo what about localization?
        is MessageModelResponse.ImageMessageModel -> "User has send a picture"
    }