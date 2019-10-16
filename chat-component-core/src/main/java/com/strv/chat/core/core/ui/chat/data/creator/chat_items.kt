package com.strv.chat.core.core.ui.chat.data.creator

import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.domain.isDayEqual
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.core.domain.runNonEmpty

object ChatItemViewListCreator :
    Creator<List<ChatItemView>, ChatItemViewListConfiguration> {

    override val create: ChatItemViewListConfiguration.() -> List<ChatItemView> = {
        messages
            .map { model ->
                ChatItemViewCreator.create(
                    ChatItemViewConfiguration(
                        currentUserId,
                        model,
                        otherMembers
                    )
                )
            }
            .let { items -> addHeaders(items) }
    }

    private fun addHeaders(messageModels: List<ChatItemView>): List<ChatItemView> =
        messageModels.fold(listOf<ChatItemView>()) { acc, chatItemView ->
            if (acc.isNotEmpty() && !acc.last().sentDate.isDayEqual(chatItemView.sentDate)) {
                acc.plus(arrayOf(ChatItemView.Header(acc.last().sentDate), chatItemView))
            } else {
                acc.plus(chatItemView)
            }
        }.runNonEmpty {
            plus(ChatItemView.Header(last().sentDate))
        }
}

class ChatItemViewListConfiguration(
    val currentUserId: String,
    val messages: List<IMessageModel>,
    val otherMembers: List<IMemberModel>
) : CreatorConfiguration