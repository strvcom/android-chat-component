package com.strv.chat.library.core.ui.conversation.mapper

import com.strv.chat.library.domain.model.ConversationModel
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView
import com.strv.chat.library.core.ui.extensions.OnClickAction

internal fun conversationItemView(list: List<ConversationModel>, memberProvider: MemberProvider, onItemClick: OnClickAction<ConversationItemView>) =
    list.map { model -> conversationItemView(model, memberProvider, onItemClick) }

private fun conversationItemView(model: ConversationModel, memberProvider: MemberProvider, onItemClick: OnClickAction<ConversationItemView>) =
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
        model.lastMessage.sentDate,
        onItemClick
    )

private fun lastMessage(messageModel: MessageModelResponse) =
    when (messageModel) {
        is MessageModelResponse.TextMessageModel -> messageModel.text
        //todo what about localization?
        is MessageModelResponse.ImageMessageModel -> "User has send a picture"
    }