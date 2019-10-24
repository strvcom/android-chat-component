package com.strv.chat.core.core.ui.conversation.data.creator

import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IImageMessageModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.ITextMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.core.domain.task.map

object ConversationItemViewCreator :
    Creator<ConversationItemView, ConversationItemViewConfiguration> {

    override val create: ConversationItemViewConfiguration.() -> ConversationItemView = {
        ConversationItemView(
            id = conversation.id,
            unread = conversation.seen[memberClient.currentUserId()]?.messageId != conversation.lastMessage.id,
            title = conversation.members
                .filter { member ->
                    member.memberId != memberClient.currentUserId()
                }.joinToString { member ->
                    member.memberName
                },
            //todo what to do if we have more users?
            pictureTask = memberClient.member(
                conversation.members.first { member ->
                    member.memberId != memberClient.currentUserId()
                }.memberId
            ).map { model -> model.memberPhotoUrl },
            message = lastMessage(conversation.lastMessage),
            sentDate = conversation.lastMessage.sentDate,
            otherMemberIds = conversation.members.filter { memberMeta ->
                memberMeta.memberId != memberClient.currentUserId()
            }.map { memberMeta ->
                memberMeta.memberId
            }
        )
    }

    private fun lastMessage(messageModel: IMessageModel) =
        when (messageModel) {
            is ITextMessageModel -> messageModel.text
            is IImageMessageModel -> chatComponent.string(R.string.user_has_sent_picture)
            else -> throw IllegalArgumentException("Unknown message type")
        }
}

class ConversationItemViewConfiguration(
    val conversation: IConversationModel,
    val memberClient: MemberClient
) : CreatorConfiguration