package com.strv.chat.firestore.model.mapper

import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.core.domain.model.MessageInputModel.ImageInputModel
import com.strv.chat.core.domain.model.MessageInputModel.TextInputModel
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.IMAGE
import com.strv.chat.firestore.entity.LAST_MESSAGE
import com.strv.chat.firestore.entity.MEMBERS
import com.strv.chat.firestore.entity.MESSAGE_TYPE
import com.strv.chat.firestore.entity.MeesageTypeEnum.IMAGE_TYPE
import com.strv.chat.firestore.entity.MeesageTypeEnum.TEXT_TYPE
import com.strv.chat.firestore.entity.ORIGINAL
import com.strv.chat.firestore.entity.SENDER_ID
import com.strv.chat.firestore.entity.messageType
import com.strv.chat.firestore.model.FirestoreConversationModel
import com.strv.chat.firestore.model.FirestoreImageMessageModel
import com.strv.chat.firestore.model.FirestoreImageModel
import com.strv.chat.firestore.model.FirestoreTextMessageModel
import strv.ktools.logE
import java.util.Date

internal fun conversationModels(list: List<FirestoreConversationEntity>) =
    list.map { entity -> conversationModel(entity) }

internal fun messageModels(list: List<FirestoreMessageEntity>) =
    list.map { entity -> messageModel(entity) }

internal fun messageEntity(id: String, messageModel: MessageInputModel) =
    FirestoreMessageEntity(
        id,
        messageModel.senderId,
        messageType(messageModel),
        dataEntity(messageModel)
    )

private fun conversationModel(entity: FirestoreConversationEntity): IConversationModel =
    FirestoreConversationModel(
        requireNotNull(entity.id) { "$ID must me specified" },
        requireNotNull(entity.members) { logE("$MEMBERS must be specified") }.also { members ->
            require(members.isNotEmpty()) { logE("$MEMBERS can not be empty") }
        },
        messageModel(requireNotNull(entity.lastMessage) { logE("$LAST_MESSAGE must be specified") })
    )

private fun messageModel(message: FirestoreMessageEntity) =
    when (messageType(requireNotNull(message.messageType) { logE("$MESSAGE_TYPE must be specified") })) {
        TEXT_TYPE -> FirestoreTextMessageModel(
            requireNotNull(message.id) { "$ID must me specified" },
            message.timestamp?.toDate() ?: Date(),
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            message.data?.message ?: ""
        )
        IMAGE_TYPE -> FirestoreImageMessageModel(
            requireNotNull(message.id) { "$ID must me specified" },
            message.timestamp?.toDate() ?: Date(),
            requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
            imageModel(requireNotNull(message.data?.image) { logE("$IMAGE must be specified") })
        )
    }

private fun imageModel(data: FirestoreImageDataEntity) =
    FirestoreImageModel(
        data.width ?: 0.0,
        data.height ?: 0.0,
        requireNotNull(data.original) { logE("$ORIGINAL must be specified") }
    )

private fun messageType(messageModel: MessageInputModel) =
    when (messageModel) {
        is TextInputModel -> TEXT_TYPE.key
        is ImageInputModel -> IMAGE_TYPE.key
    }

private fun dataEntity(messageModel: MessageInputModel) =
    when (messageModel) {
        is TextInputModel -> textDataEntity(messageModel)
        is ImageInputModel -> imageDataEntity(messageModel)
    }

private fun textDataEntity(messageModel: TextInputModel) =
    FirestoreDataEntity(
        message = messageModel.text
    )

private fun imageDataEntity(messageModel: ImageInputModel) =
    FirestoreDataEntity(
        image = FirestoreImageDataEntity(
            messageModel.imageModel.width,
            messageModel.imageModel.height,
            messageModel.imageModel.original
        )
    )

