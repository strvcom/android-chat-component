package com.strv.chat.library.firestore.entity

import androidx.annotation.StringDef
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.strv.chat.library.data.entity.SourceEntity
import strv.ktools.logE

const val SENDER_ID = "sender_id"
const val TIMESTAMP = "timestamp"
const val MESSAGE_TYPE = "message_type"
const val DATA = "data"
const val MESSAGE_ID = "message_id"
const val MESSAGE = "message"
const val IMAGE = "image"
const val WIDTH = "width"
const val HEIGHT = "height"
const val ORIGINAL = "original"

const val TEXT_TYPE = "text"
const val IMAGE_TYPE = "image"

@Retention(AnnotationRetention.SOURCE)
@StringDef(TEXT_TYPE, IMAGE_TYPE)
annotation class MessageType

data class FirestoreMessage(
    @get:PropertyName(SENDER_ID) @set:PropertyName(SENDER_ID) var senderId: String? = null,
    @get:PropertyName(TIMESTAMP) @set:PropertyName(TIMESTAMP) var timestamp: Timestamp? = null,
    @get:PropertyName(MESSAGE_TYPE) @set:PropertyName(MESSAGE_TYPE) @MessageType var messageType: String? = null,
    @get:PropertyName(DATA) @set:PropertyName(DATA) var data: FirestoreData? = null
) : SourceEntity() {

    fun toMap() = hashMapOf(
        SENDER_ID to requireNotNull(senderId) { logE("$SENDER_ID must be specified") },
        TIMESTAMP to FieldValue.serverTimestamp(),
        MESSAGE_TYPE to requireNotNull(messageType) { logE("$MESSAGE_TYPE must be specified") },
        DATA to requireNotNull(data) { logE("$DATA must be specified") }.run {
            if (messageType == TEXT_TYPE) toTextMap() else toImageMap()
        }
    )

    fun toLastMessageMap() = hashMapOf(
        MESSAGE_ID to requireNotNull(id) { logE("$MESSAGE_ID must be specified") },
        SENDER_ID to requireNotNull(senderId) { logE("$SENDER_ID must be specified") },
        TIMESTAMP to FieldValue.serverTimestamp(),
        MESSAGE_TYPE to requireNotNull(messageType) { logE("$MESSAGE_TYPE must be specified") },
        DATA to requireNotNull(data) { logE("$DATA must be specified") }.run {
            if (messageType == TEXT_TYPE) toTextMap() else toImageMap()
        }
    )
}

data class FirestoreData(
    var message: String? = null,
    var image: FirestoreImageData? = null
) {

    fun toTextMap() = hashMapOf(
        MESSAGE to requireNotNull(message) { logE("$MESSAGE must be specified") }
    )

    fun toImageMap() = hashMapOf(
        IMAGE to requireNotNull(image?.toMap()) { logE("$IMAGE must be specified") }
    )
}

data class FirestoreImageData(
    var width: Int? = null,
    var height: Int? = null,
    var original: String? = null
) {

    fun toMap() = hashMapOf(
        WIDTH to (width ?: 0),
        HEIGHT to (height ?: 0),
        ORIGINAL to requireNotNull(original) { logE("$ORIGINAL must be specified") }
    )
}