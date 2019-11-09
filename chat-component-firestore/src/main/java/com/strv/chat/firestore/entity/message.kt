package com.strv.chat.firestore.entity

import androidx.annotation.StringDef
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.data.entity.SourceEntity
import strv.ktools.logE

internal const val SENDER_ID = "sender_id"
internal const val TIMESTAMP = "timestamp"
internal const val MESSAGE_TYPE = "message_type"
internal const val DATA = "data"
internal const val MESSAGE = "message"
internal const val IMAGE = "image"
internal const val IMAGE_URL = "image_url"

internal const val KEY_TEXT_TYPE = "text"
internal const val KEY_IMAGE_TYPE = "image"

@Retention(AnnotationRetention.SOURCE)
@StringDef(KEY_TEXT_TYPE, KEY_IMAGE_TYPE)
internal annotation class MessageType

internal enum class MeesageTypeEnum(val key: String) {
    TEXT_TYPE(KEY_TEXT_TYPE),
    IMAGE_TYPE(KEY_IMAGE_TYPE)
}

internal fun messageType(key: String): MeesageTypeEnum =
    enumValues<MeesageTypeEnum>().first {
        it.key.equals(key, true)
    }

internal data class FirestoreMessageEntity(
    @get:PropertyName(ID) @set:PropertyName(ID) override var id: String? = null,
    @get:PropertyName(SENDER_ID) @set:PropertyName(SENDER_ID) var senderId: String? = null,
    @get:PropertyName(MESSAGE_TYPE) @set:PropertyName(MESSAGE_TYPE) @MessageType var messageType: String? = null,
    @get:PropertyName(DATA) @set:PropertyName(DATA) var data: FirestoreDataEntity? = null,
    @get:PropertyName(TIMESTAMP) @set:PropertyName(TIMESTAMP) var timestamp: Timestamp? = null
) : SourceEntity {

    fun toMap() = hashMapOf(
        SENDER_ID to requireNotNull(senderId) { logE("$SENDER_ID must be specified") },
        TIMESTAMP to FieldValue.serverTimestamp(),
        MESSAGE_TYPE to requireNotNull(messageType) { logE("$MESSAGE_TYPE must be specified") },
        DATA to requireNotNull(data) { logE("$DATA must be specified") }.run {
            if (messageType == KEY_TEXT_TYPE) toTextMap() else toImageMap()
        }
    )

    fun toLastMessageMap() = hashMapOf(
        ID to requireNotNull(id) { logE("$ID must be specified") },
        SENDER_ID to requireNotNull(senderId) { logE("$SENDER_ID must be specified") },
        TIMESTAMP to FieldValue.serverTimestamp(),
        MESSAGE_TYPE to requireNotNull(messageType) { logE("$MESSAGE_TYPE must be specified") },
        DATA to requireNotNull(data) { logE("$DATA must be specified") }.run {
            if (messageType == KEY_TEXT_TYPE) toTextMap() else toImageMap()
        }
    )
}

internal data class FirestoreDataEntity(
    @get:PropertyName(MESSAGE) @set:PropertyName(MESSAGE) var message: String? = null,
    @get:PropertyName(IMAGE) @set:PropertyName(IMAGE) var image: FirestoreImageDataEntity? = null
) {

    fun toTextMap() = hashMapOf(
        MESSAGE to requireNotNull(message) { logE("$MESSAGE must be specified") }
    )

    fun toImageMap() = hashMapOf(
        IMAGE to requireNotNull(image?.toMap()) { logE("$IMAGE must be specified") }
    )
}

internal data class FirestoreImageDataEntity(
    @get:PropertyName(IMAGE_URL) @set:PropertyName(IMAGE_URL) var url: String? = null
) {

    fun toMap() = hashMapOf(
        IMAGE_URL to requireNotNull(url) { logE("$IMAGE_URL must be specified") }
    )
}