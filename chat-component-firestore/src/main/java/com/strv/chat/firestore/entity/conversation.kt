package com.strv.chat.firestore.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.data.entity.SourceEntity

const val LAST_MESSAGE = "last_message"
const val MESSAGE_ID = "message_id"
const val MEMBERS = "members"
const val MEMBERS_META = "members_meta"
const val MEMBER_NAME = "name"
const val SEEN = "seen"

data class FirestoreConversationEntity(
    @get:PropertyName(ID) @set:PropertyName(ID) override var id: String? = null,
    @get:PropertyName(LAST_MESSAGE) @set:PropertyName(LAST_MESSAGE) var lastMessage: FirestoreMessageEntity? = null,
    @get:PropertyName(MEMBERS) @set:PropertyName(MEMBERS) var members: List<String>? = null,
    @get:PropertyName(MEMBERS_META) @set:PropertyName(MEMBERS_META) var membersMeta: Map<String, FirestoreMemberMetaEntity>? = null,
    @get:PropertyName(SEEN) @set:PropertyName(SEEN) var seen: Map<String, FirestoreSeenEntity?>? = null
) : SourceEntity {

    fun toMap(): HashMap<String, Any?> = hashMapOf(
        LAST_MESSAGE to lastMessage?.toMap(),
        MEMBERS to members,
        MEMBERS_META to membersMeta,
        SEEN to seen
    )
}

data class FirestoreSeenEntity(
    @get:PropertyName(MESSAGE_ID) @set:PropertyName(MESSAGE_ID) var messageId: String? = null,
    @ServerTimestamp var timestamp: Timestamp? = null
) {

    fun toMap(serverTimestamp: Boolean = true): HashMap<String, Any?> = hashMapOf(
        MESSAGE_ID to messageId,
        TIMESTAMP to if (serverTimestamp) FieldValue.serverTimestamp() else timestamp
    )
}

data class FirestoreMemberMetaEntity(
    @get:PropertyName(MEMBER_NAME) @set:PropertyName(MEMBER_NAME) var name: String? = null
) {

    fun toMap(): HashMap<String, Any?> = hashMapOf(
        MEMBER_NAME to name
    )
}