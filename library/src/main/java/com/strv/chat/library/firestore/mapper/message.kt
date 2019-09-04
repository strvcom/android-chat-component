package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.data.mapper.Mapper
import com.strv.chat.library.domain.model.ChatItem
import com.strv.chat.library.domain.model.Message
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.entity.SENDER_ID
import com.strv.chat.library.firestore.entity.TEXT_TYPE
import strv.ktools.logE

object MessageMapper: Mapper<FirestoreMessage, ChatItem> {

    override fun mapToDomain(entity: FirestoreMessage): ChatItem =
        when (entity.messageType) {
            TEXT_TYPE -> ChatItem.ChatMessage(
                requireNotNull(entity.senderId) { logE("$SENDER_ID must be specified") },
                "Name",
                "Url",
                textMessage(entity)
            )
            else -> ChatItem.ChatHeader(entity.timestamp!!.toDate())
        }

    override fun mapToEntity(domain: ChatItem): FirestoreMessage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun textMessage(entity: FirestoreMessage) =
        Message.TextMessage(entity.timestamp!!.toDate(), entity.data?.message ?: "")

}