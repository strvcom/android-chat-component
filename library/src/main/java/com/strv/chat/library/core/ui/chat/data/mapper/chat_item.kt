package com.strv.chat.library.core.ui.chat.data.mapper

import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.core.ui.data.mapper.memberView
import com.strv.chat.library.domain.isDayEqual
import com.strv.chat.library.domain.model.IImageMessageModel
import com.strv.chat.library.domain.model.IMessageModel
import com.strv.chat.library.domain.model.ITextMessageModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.domain.runNonEmpty

internal fun chatItemView(
    list: List<IMessageModel>,
    memberProvider: MemberProvider
) =
    list
        .map { model -> chatItemView(model, memberProvider) }
        .let { items -> addHeaders(items) }

private fun addHeaders(messageModels: List<ChatItemView>): List<ChatItemView> =
    messageModels.fold(listOf<ChatItemView>()) { acc, chatItemView ->
        if (acc.isNotEmpty() && !acc.last().sentDate.isDayEqual(chatItemView.sentDate)) {
            acc.plus(arrayOf(Header(acc.last().sentDate), chatItemView))
        } else {
            acc.plus(chatItemView)
        }
    }.runNonEmpty {
        plus(Header(last().sentDate))
    }

private fun chatItemView(
    model: IMessageModel,
    memberProvider: MemberProvider
) =
    when (model) {
        is ITextMessageModel -> {
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
        is IImageMessageModel -> {
            if (memberProvider.currentUserId() == model.senderId) {
                MyImageMessage(
                    model.id,
                    model.sentDate,
                    model.imageModel.original
                )
            } else {
                OtherImageMessage(
                    model.id,
                    model.sentDate,
                    model.imageModel.original,
                    memberView(memberProvider.member(model.senderId))
                )
            }
        }
        else -> throw IllegalArgumentException("Unknown message type")
    }