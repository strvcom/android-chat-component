package com.strv.chat.firestore.entity.creator

import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreDataEntity
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.MeesageTypeEnum

object MessageEntityCreator : Creator<FirestoreMessageEntity, MessageEntityConfiguration> {

    override val create: MessageEntityConfiguration.() -> FirestoreMessageEntity = {
        FirestoreMessageEntity(
            id,
            messageInputModel.senderId,
            messageType(messageInputModel),
            dataEntity(messageInputModel)
        )
    }

    private fun messageType(messageModel: MessageInputModel) =
        when (messageModel) {
            is MessageInputModel.TextInputModel -> MeesageTypeEnum.TEXT_TYPE.key
            is MessageInputModel.ImageInputModel -> MeesageTypeEnum.IMAGE_TYPE.key
        }

    private fun dataEntity(messageModel: MessageInputModel) =
        when (messageModel) {
            is MessageInputModel.TextInputModel -> textDataEntity(messageModel)
            is MessageInputModel.ImageInputModel -> imageDataEntity(messageModel)
        }

    private fun textDataEntity(messageModel: MessageInputModel.TextInputModel) =
        FirestoreDataEntity(
            message = messageModel.text
        )

    private fun imageDataEntity(messageModel: MessageInputModel.ImageInputModel) =
        FirestoreDataEntity(
            image = FirestoreImageDataEntity(
                messageModel.imageModel.width,
                messageModel.imageModel.height,
                messageModel.imageModel.original
            )
        )
}

class MessageEntityConfiguration(
    val id: String,
    val messageInputModel: MessageInputModel
) : CreatorConfiguration