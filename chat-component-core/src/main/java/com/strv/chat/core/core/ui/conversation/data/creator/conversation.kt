package com.strv.chat.core.core.ui.conversation.data.creator

import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.core.ui.data.creator.MemberViewsConfiguration
import com.strv.chat.core.core.ui.data.creator.MemberViewsCreator
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.map
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IImageMessageModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.ITextMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration

object ConversationItemViewCreator :
    Creator<ConversationItemView, ConversationItemViewConfiguration> {

    override val create: ConversationItemViewConfiguration.() -> ConversationItemView = {
        ConversationItemView(
            conversation.id,
            conversation.seen[memberClient.currentUserId()]?.messageId != conversation.lastMessage.id,
            memberClient.members(
                conversation.members
                    .filter { memberId ->
                        memberId != memberClient.currentUserId()
                    }
            ).map { modelList ->
                MemberViewsCreator.create(MemberViewsConfiguration(modelList))
            },
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
    val memberClient: MemberClient
) : CreatorConfiguration