package com.strv.chat.library.core.ui.chat.data.mapper

import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.core.ui.data.mapper.memberView
import com.strv.chat.library.domain.isDayEqual
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.model.MessageModelResponse.ImageMessageModel
import com.strv.chat.library.domain.model.MessageModelResponse.TextMessageModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.domain.runNonEmpty

internal fun chatItemView(
    list: List<MessageModelResponse>,
    memberProvider: MemberProvider
) =
    list
        .map { model -> chatItemView(model, memberProvider) }
        .let { items -> addHeaders(items) }

private fun addHeaders(messageModels: List<ChatItemView>): List<ChatItemView> =
    messageModels.fold(listOf<ChatItemView>()) { acc, chatItemView ->
        if (acc.isNotEmpty() && !acc.last().sentDate.isDayEqual(chatItemView.sentDate)) {
            acc.plus(arrayOf(chatItemView, Header(chatItemView.sentDate)))
        } else {
            acc.plus(chatItemView)
        }
    }.runNonEmpty {
        plus(Header(last().sentDate))
    }

private fun chatItemView(
    model: MessageModelResponse,
    memberProvider: MemberProvider
) =
    when (model) {
        is TextMessageModel -> {
            if (memberProvider.currentUserId() == model.senderId) {
                MyTextMessage(
                    model.id,
                    model.sentDate,
                    model.text
                )
            } else {
                OtherTextMessage(
                    model.id,
                    model.sentDate,
                    memberView(memberProvider.member(model.senderId)),
                    model.text
                )
            }
        }
        is ImageMessageModel -> {
            if (memberProvider.currentUserId() == model.senderId) {
                MyImageMessage(
                    model.id,
                    model.sentDate,
                    model.image.original
                )
            } else {
                OtherImageMessage(
                    model.id,
                    model.sentDate,
                    model.image.original,
                    memberView(memberProvider.member(model.senderId))
                )
            }
        }
    }