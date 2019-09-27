package com.strv.chat.library.firestore.entity

import com.google.firebase.firestore.PropertyName
import com.strv.chat.library.data.entity.ID
import com.strv.chat.library.data.entity.SourceEntity

const val LAST_MESSAGE = "last_message"
const val MEMBERS = "members"

data class FirestoreConversationEntity(
    @get:PropertyName(ID) @set:PropertyName(ID) override var id: String? = null,
    @get:PropertyName(LAST_MESSAGE) @set:PropertyName(LAST_MESSAGE) var lastMessage: FirestoreMessageEntity? = null,
    @get:PropertyName(MEMBERS) @set:PropertyName(MEMBERS) var members: List<String>? = null
) : SourceEntity {

    fun toMap(): HashMap<String, Any?> = hashMapOf(
        "last_message" to lastMessage?.toMap(),
        "members" to members
    )
}