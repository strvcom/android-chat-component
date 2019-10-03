package com.strv.chat.core.core.ui.conversation.data.creator

import com.strv.chat.core.core.ui.conversation.data.ConversationItemView
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.core.domain.provider.MemberProvider

object ConversationItemViewListCreator :
    Creator<List<ConversationItemView>, ConversationItemViewListConfiguration> {

    override val create: ConversationItemViewListConfiguration.() -> List<ConversationItemView> = {
        conversations.map { model ->
            ConversationItemViewCreator.create(
                ConversationItemViewConfiguration(model, memberProvider)
            )
        }
    }
}

class ConversationItemViewListConfiguration(
    val conversations: List<IConversationModel>,
    val memberProvider: MemberProvider
) : CreatorConfiguration