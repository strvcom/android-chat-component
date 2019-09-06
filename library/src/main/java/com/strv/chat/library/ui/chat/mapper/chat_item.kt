package com.strv.chat.library.ui.chat.mapper

import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.ui.chat.view.ChatItemView
import com.strv.chat.library.ui.chat.view.ChatItemView.*
import com.strv.chat.library.ui.chat.view.MemberView

object ChatItemsMapper {

    fun mapToView(
        userId: String,
        members: List<MemberView>,
        models: List<MessageModel>
    ): List<ChatItemView> =
        models.map { model ->
            chatMessage(userId, members, model)
        }

    fun mapToDomain(domain: List<ChatItemView>): List<MessageModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun chatMessage(userId: String, members: List<MemberView>, model: MessageModel) =
        when (model) {
            is MessageModel.TextMessageModel -> {
                if (userId == model.senderId) {
                    MyTextMessage(
                        model.id,
                        model.sentDate,
                        model.text
                    )
                } else {
                    OtherTextMessage(
                        model.id,
                        model.sentDate,
                        members.first { model.senderId == it.userId },
                        model.text
                    )
                }
            }
            is MessageModel.ImageMessageModel -> TODO()
        }


//    private fun addHeaders(chatMessages: List<ChatMessageView<Message>>): MutableList<ChatItemView> {
//        var lastHeaderDate = Date(0)
//        val chatItems = mutableListOf<ChatItemView>()
//
//        chatMessages.forEach { message ->
//            if (!lastHeaderDate.isDayEqual(message.message.sentDate)) {
//                chatItems.add(ChatHeaderView(message.message.sentDate))
//                lastHeaderDate = message.message.sentDate
//            }
//            chatItems.add(message)
//        }
//
//        return chatItems
//    }
}