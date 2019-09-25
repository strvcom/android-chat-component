package com.strv.chat.library.core.ui.chat.data.mapper

import com.strv.chat.library.domain.isDayEqual
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.domain.model.MessageModelResponse.ImageMessageModel
import com.strv.chat.library.domain.model.MessageModelResponse.TextMessageModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.domain.runNonEmpty
import com.strv.chat.library.core.ui.chat.data.ChatItemView
import com.strv.chat.library.core.ui.chat.data.ChatItemView.*
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.MyImageMessage
import com.strv.chat.library.core.ui.chat.data.ChatItemView.Image.OtherImageMessage
import com.strv.chat.library.core.ui.data.mapper.memberView
import com.strv.chat.library.core.ui.extensions.OnClickAction

internal fun chatItemView(
    list: List<MessageModelResponse>,
    memberProvider: MemberProvider,
    clickAction: OnClickAction<Image>? = null
) =
    list
        .map { model -> chatItemView(model, memberProvider, clickAction) }
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
    memberProvider: MemberProvider,
    clickAction: OnClickAction<Image>? = null
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
                    model.image.original,
                    clickAction
                )
            } else {
                OtherImageMessage(
                    model.id,
                    model.sentDate,
                    model.image.original,
                    clickAction,
                    memberView(memberProvider.member(model.senderId))
                )
            }
        }
    }