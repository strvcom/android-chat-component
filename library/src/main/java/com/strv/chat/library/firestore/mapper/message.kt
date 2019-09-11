package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.data.entity.ID
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelResponse
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

internal fun messageEntity(messageModel: MessageModelRequest) =
    FirestoreMessage(
        messageModel.senderId,
        messageType(messageModel),
        dataEntity(messageModel)
    )

internal fun messageModels(list: List<FirestoreMessage>) =
    list.map { entity -> messageModels(entity) }

private fun messageModels(message: FirestoreMessage) =
    when (messageType(requireNotNull(message.messageType) { "$MESSAGE_TYPE must be specified" })) {
        TEXT_TYPE -> MessageModelResponse.TextMessageModel(
            requireNotNull(message.id) { "$ID must me specified" },
            message.timestamp?.toDate() ?: Date(),
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            message.data?.message ?: ""
        )
        IMAGE_TYPE -> MessageModelResponse.ImageMessageModel(
            requireNotNull(message.id) { "$ID must me specified" },
            requireNotNull(message.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            imageModel(requireNotNull(message.data?.image) { logE("$IMAGE must be specified") })
        )
    }

private fun imageModel(data: FirestoreImageData) =
    MessageModelResponse.ImageMessageModel.Image(
        data.width ?: 0.0,
        data.height ?: 0.0,
        requireNotNull(data.original) { logE("$ORIGINAL must be specified") }
    )

private fun messageType(messageModel: MessageModelRequest) =
    when (messageModel) {
        is MessageModelRequest.TextMessageModel -> TEXT_TYPE.key
        is MessageModelRequest.ImageMessageModel -> IMAGE_TYPE.key
    }

private fun dataEntity(messageModel: MessageModelRequest) =
    when (messageModel) {
        is MessageModelRequest.TextMessageModel -> textDataEntity(messageModel)
        is MessageModelRequest.ImageMessageModel -> imageDataEntity(messageModel)
    }

private fun textDataEntity(messageModel: MessageModelRequest.TextMessageModel) =
    FirestoreData(
        message = messageModel.text
    )

private fun imageDataEntity(messageModel: MessageModelRequest.ImageMessageModel) =
    FirestoreData(
        image = FirestoreImageData(
            messageModel.image.width,
            messageModel.image.height,
            messageModel.image.original
        )
    )

