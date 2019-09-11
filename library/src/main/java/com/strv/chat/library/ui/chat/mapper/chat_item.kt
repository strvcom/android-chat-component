package com.strv.chat.library.ui.chat.mapper

import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.domain.model.MessageModel.TextMessageModel
import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.ui.chat.data.MemberView

internal fun chatItemView(list: List<MessageModel>, memberProvider: MemberProvider) =
    list.map { model -> chatItemView(model, memberProvider) }

private fun chatItemView(model: MessageModel, memberProvider: MemberProvider) =
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
        is MessageModel.ImageMessageModel -> TODO()
    }

private fun memberView(memberModel: MemberModel) =
    MemberView(memberModel.userId, memberModel.userName, memberModel.userPhotoUrl)