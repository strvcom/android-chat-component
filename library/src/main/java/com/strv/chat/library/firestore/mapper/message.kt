package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.domain.model.MessageModel.ImageMessageModel
import com.strv.chat.library.domain.model.MessageModel.TextMessageModel
import com.strv.chat.library.firestore.entity.FirestoreData
import com.strv.chat.library.firestore.entity.FirestoreImageData
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.entity.IMAGE
import com.strv.chat.library.firestore.entity.MESSAGE_TYPE
import com.strv.chat.library.firestore.entity.MeesageTypeEnum.IMAGE_TYPE
import com.strv.chat.library.firestore.entity.MeesageTypeEnum.TEXT_TYPE
import com.strv.chat.library.firestore.entity.ORIGINAL
import com.strv.chat.library.firestore.entity.SENDER_ID
import com.strv.chat.library.firestore.entity.TIMESTAMP
import com.strv.chat.library.firestore.entity.messageType
import strv.ktools.logE
import java.util.*

internal fun messageEntity(messageModel: MessageModel) =
    FirestoreMessage(
        messageModel.senderId,
        messageType(messageModel),
        dataEntity(messageModel)
    )

internal fun messageModels(list: List<FirestoreMessage>) =
    list.map { entity -> messageModels(entity) }

private fun messageModels(message: FirestoreMessage) =
    when (messageType(requireNotNull(message.messageType) { "$MESSAGE_TYPE must be specified" })) {
        TEXT_TYPE -> TextMessageModel(
            message.timestamp?.toDate() ?: Date(),
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            message.data?.message ?: ""
        )
        IMAGE_TYPE -> ImageMessageModel(
            requireNotNull(message.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            imageModel(requireNotNull(message.data?.image) { logE("$IMAGE must be specified") })
        )
    }

private fun imageModel(data: FirestoreImageData) =
    ImageMessageModel.Image(
        data.width ?: 0.0,
        data.height ?: 0.0,
        requireNotNull(data.original) { logE("$ORIGINAL must be specified") }
    )

private fun messageType(messageModel: MessageModel) =
    when (messageModel) {
        is TextMessageModel -> TEXT_TYPE.key
        is ImageMessageModel -> IMAGE_TYPE.key
    }

private fun dataEntity(messageModel: MessageModel) =
    when (messageModel) {
        is TextMessageModel -> textDataEntity(messageModel)
        is ImageMessageModel -> imageDataEntity(messageModel)
    }

private fun textDataEntity(messageModel: TextMessageModel) =
    FirestoreData(
        message = messageModel.text
    )

private fun imageDataEntity(messageModel: ImageMessageModel) =
    FirestoreData(
        image = FirestoreImageData(
            messageModel.image.width,
            messageModel.image.height,
            messageModel.image.original
        )
    )

