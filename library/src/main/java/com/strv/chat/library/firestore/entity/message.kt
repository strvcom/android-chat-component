package com.strv.chat.library.firestore.entity

import androidx.annotation.StringDef
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.strv.chat.library.data.entity.ID
import com.strv.chat.library.data.entity.SourceEntity
import strv.ktools.logE

const val SENDER_ID = "sender_id"
const val TIMESTAMP = "timestamp"
const val MESSAGE_TYPE = "message_type"
const val DATA = "data"
const val MESSAGE = "message"
const val IMAGE = "image"
const val WIDTH = "width"
const val HEIGHT = "height"
const val ORIGINAL = "original"

const val KEY_TEXT_TYPE = "text"
const val KEY_IMAGE_TYPE = "image"

@Retention(AnnotationRetention.SOURCE)
@StringDef(KEY_TEXT_TYPE, KEY_IMAGE_TYPE)
annotation class MessageType

enum class MeesageTypeEnum(val key: String) {
    TEXT_TYPE(KEY_TEXT_TYPE),
    IMAGE_TYPE(KEY_IMAGE_TYPE)
}

fun messageType(key: String): MeesageTypeEnum =
    enumValues<MeesageTypeEnum>().first {
        it.key.equals(key, true)
    }

data class FirestoreMessageEntity(
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

data class FirestoreDataEntity(
    var message: String? = null,
    var image: FirestoreImageDataEntity? = null
) {

    fun toTextMap() = hashMapOf(
        MESSAGE to requireNotNull(message) { logE("$MESSAGE must be specified") }
    )

    fun toImageMap() = hashMapOf(
        IMAGE to requireNotNull(image?.toMap()) { logE("$IMAGE must be specified") }
    )
}

data class FirestoreImageDataEntity(
    var width: Double? = null,
    var height: Double? = null,
    var original: String? = null
) {

    fun toMap() = hashMapOf(
        WIDTH to (width ?: 0),
        HEIGHT to (height ?: 0),
        ORIGINAL to requireNotNull(original) { logE("$ORIGINAL must be specified") }
    )
}