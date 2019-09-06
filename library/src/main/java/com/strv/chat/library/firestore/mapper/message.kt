package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.data.entity.ID
import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.domain.model.MessageModel.*
import com.strv.chat.library.firestore.entity.*
import com.strv.chat.library.firestore.entity.MeesageTypeEnum.*
import strv.ktools.logE

internal object MessageMapper {

    fun mapToDomain(entity: FirestoreMessage): MessageModel =
        when (messageType(requireNotNull(entity.messageType) { "$MESSAGE_TYPE must be specified" })) {
            TEXT_TYPE -> TextMessageModel(
                requireNotNull(entity.id) { logE("$ID must be specified") },
                requireNotNull(entity.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
                requireNotNull(entity.senderId) { logE("$SENDER_ID must be specified") },
                entity.data?.message ?: ""
            )
            IMAGE_TYPE -> ImageMessageModel(
                requireNotNull(entity.id) { logE("$ID must be specified") },
                requireNotNull(entity.timestamp?.toDate()) { logE("$TIMESTAMP must be specified") },
                requireNotNull(entity.senderId) { logE("$SENDER_ID must be specified") },
                image(requireNotNull(entity.data?.image) { logE("$IMAGE must be specified") })
            )
        }

    fun mapToEntity(domain: MessageModel): FirestoreMessage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun image(data: FirestoreImageData) =
        ImageMessageModel.Image(
            data.width ?: 0.0,
            data.height ?: 0.0,
            requireNotNull(data.original) { logE("$ORIGINAL must be specified") }
        )
}