package com.strv.chat.core.core.ui.chat.data.creator

import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.data.creator.MemberViewConfiguration
import com.strv.chat.core.core.ui.data.creator.MemberViewCreator
import com.strv.chat.core.domain.model.IImageMessageModel
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.ITextMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration

object ChatItemViewCreator :
    Creator<ChatItemView, ChatItemViewConfiguration> {

    override val create: ChatItemViewConfiguration.() -> ChatItemView = {
        when (message) {
            is ITextMessageModel -> {
                if (currentUserId == message.senderId) {
                    ChatItemView.MyTextMessage(
                        message.id,
                        message.sentDate,
                        message.text
                    )
                } else {
                    ChatItemView.OtherTextMessage(
                        message.id,
                        message.sentDate,
                        MemberViewCreator.create(
                            MemberViewConfiguration(
                                otherMembers.first { member ->
                                    member.memberId == message.senderId
                                }
                            )
                        ),
                        message.text
                    )
                }
            }
            is IImageMessageModel -> {
                if (currentUserId == message.senderId) {
                    ChatItemView.MyImageMessage(
                        message.id,
                        message.sentDate,
                        message.imageModel.original
                    )
                } else {
                    ChatItemView.OtherImageMessage(
                        message.id,
                        message.sentDate,
                        message.imageModel.original,
                        MemberViewCreator.create(
                            MemberViewConfiguration(
                                otherMembers.first { member ->
                                    member.memberId == message.senderId
                                }
                            )
                        )
                    )
                }
            }
            else -> throw IllegalArgumentException("Unknown message type")
        }
    }
}

class ChatItemViewConfiguration(
    val currentUserId: String,
    val message: IMessageModel,
    val otherMembers: List<IMemberModel>
) : CreatorConfiguration