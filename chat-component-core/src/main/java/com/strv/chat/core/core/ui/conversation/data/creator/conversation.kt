package com.strv.chat.core.core.ui.conversation.data.creator

import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IImageMessageModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.ITextMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.core.domain.provider.MemberProvider

object ConversationItemViewCreator :
    Creator<ConversationItemView, ConversationItemViewConfiguration> {

    override val create: ConversationItemViewConfiguration.() -> ConversationItemView = {
        ConversationItemView(
            conversation.id,
            conversation.seen[memberProvider.currentUserId()]?.messageId != conversation.lastMessage.id,
            //todo what if there is just one user/more than two users
            conversation.members
                .filterNot { memberId ->
                    memberId != memberProvider.currentUserId()
                }.map { memberId ->
                    memberProvider.member(memberId).userPhotoUrl
                }.first(),
            conversation.members
                .filterNot { memberId ->
                    memberId != memberProvider.currentUserId()
                }.fold(listOf<String>()) { acc, id ->
                    acc.plus(memberProvider.member(id).userName)
                }.joinToString(),
            lastMessage(conversation.lastMessage),
            conversation.lastMessage.sentDate
        )
    }

    private fun lastMessage(messageModel: IMessageModel) =
        when (messageModel) {
            is ITextMessageModel -> messageModel.text
            is IImageMessageModel -> ChatComponent.string(R.string.user_has_sent_picture)
            else -> throw IllegalArgumentException("Unknown message type")
        }
}

class ConversationItemViewConfiguration(
    val conversation: IConversationModel,
    val memberProvider: MemberProvider
) : CreatorConfiguration