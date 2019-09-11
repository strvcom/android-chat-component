package com.strv.chat.library.firestore.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.strv.chat.library.data.entity.SourceEntity

data class FirestoreSeen(
    @get:PropertyName("message_id") @set:PropertyName("message_id") var messageId: String? = null,
    @ServerTimestamp var timestamp: Timestamp? = null
) : SourceEntity() {

    fun toMap(): HashMap<String, Any?> = hashMapOf(
        "message_id" to messageId,
        "timestamp" to FieldValue.serverTimestamp()
    )
}