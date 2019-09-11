package com.strv.chat.library.ui.chat.mapper

import com.strv.chat.library.domain.isDayEqual
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.model.MessageModelResponse.TextMessageModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.domain.runNonEmpty
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.ui.data.mapper.memberView

internal fun chatItemView(list: List<MessageModelResponse>, memberProvider: MemberProvider) =
    list
        .map { model -> chatItemView(model, memberProvider) }
        .let { items -> addHeaders(items) }

private fun addHeaders(messageModels: List<ChatItemView>): List<ChatItemView> =
    messageModels.fold(listOf<ChatItemView>()) { acc, chatItemView ->
        if (acc.isNotEmpty() && !acc.last().sentDate.isDayEqual(chatItemView.sentDate)) {
            acc.plus(arrayOf(chatItemView, Header(chatItemView.sentDate)))
        }  else {
            acc.plus(chatItemView)
        }
    }.runNonEmpty {
        plus(Header(last().sentDate))
    }

private fun chatItemView(model: MessageModelResponse, memberProvider: MemberProvider) =
    when (model) {
        is TextMessageModel -> {
            if (memberProvider.currentUserId() == model.senderId) {
                MyTextMessage(
                    model.sentDate,
                    model.text
                )
            } else {
                OtherTextMessage(
                    model.sentDate,
                    memberView(memberProvider.member(model.senderId)),
                    model.text
                )
            }
        }
        is MessageModelResponse.ImageMessageModel -> TODO()
    }