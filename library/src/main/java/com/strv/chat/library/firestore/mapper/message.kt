package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.domain.model.MessageModel.ImageMessageModel
import com.strv.chat.library.domain.model.MessageModel.TextMessageModel
import com.strv.chat.library.firestore.entity.FirestoreImageData
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.entity.IMAGE
import com.strv.chat.library.firestore.entity.MESSAGE_TYPE
import com.strv.chat.library.firestore.entity.MeesageTypeEnum.TEXT_TYPE
import com.strv.chat.library.firestore.entity.MeesageTypeEnum.IMAGE_TYPE
import com.strv.chat.library.firestore.entity.ORIGINAL
import com.strv.chat.library.firestore.entity.SENDER_ID
import com.strv.chat.library.firestore.entity.TIMESTAMP
import com.strv.chat.library.firestore.entity.messageType
import strv.ktools.logE

internal fun messageModel(list: List<FirestoreMessage>) = list.map { entity -> messageModel(entity) }

private fun messageModel(message: FirestoreMessage) =
    when (messageType(requireNotNull(message.messageType) { "$MESSAGE_TYPE must be specified" })) {
        TEXT_TYPE -> TextMessageModel(
            requireNotNull(message.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            message.data?.message ?: ""
        )
        IMAGE_TYPE -> ImageMessageModel(
            requireNotNull(message.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            image(requireNotNull(message.data?.image) { logE("$IMAGE must be specified") })
        )
    }

private fun image(data: FirestoreImageData) =
    ImageMessageModel.Image(
        data.width ?: 0.0,
        data.height ?: 0.0,
        requireNotNull(data.original) { logE("$ORIGINAL must be specified") }
    )