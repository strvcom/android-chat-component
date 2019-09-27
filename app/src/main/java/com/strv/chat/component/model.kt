package com.strv.chat.component

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

const val CONVERSATIONS_COLLECTION = "conversations"
const val MESSAGES_COLLECTION = "messages"

abstract class FirestoreDocument(
    var id: String? = null
) {

    abstract fun toMap(): HashMap<String, Any?>
}

data class FirestoreConversation(
    @get:PropertyName("last_message") @set:PropertyName("last_message") var lastMessage: FirestoreMessage? = null,
    var members: List<String>? = null
) : FirestoreDocument() {

    override fun toMap(): HashMap<String, Any?> = hashMapOf(
        "last_message" to lastMessage?.toMap(),
        "members" to members
    )
}

data class FirestoreSeen(
    @get:PropertyName("message_id") @set:PropertyName("message_id") var messageId: String? = null,
    @ServerTimestamp var timestamp: Timestamp? = null
) : FirestoreDocument() {

    override fun toMap(): HashMap<String, Any?> = hashMapOf(
        "message_id" to messageId,
        "timestamp" to null
    )
}


data class FirestoreMessage(
    @get:PropertyName("sender_id") @set:PropertyName("sender_id") var senderId: String? = null,
    @ServerTimestamp var timestamp: Timestamp? = null,
    @get:PropertyName("message_type") @set:PropertyName("messageType") var messageType: String? = null,
    var data: FirestoreData? = null
) {

    fun toMap() = hashMapOf(
        "sender_id" to senderId,
        "timestamp" to FieldValue.serverTimestamp(),
        "message_type" to messageType,
        "data" to data?.toMap()
    )
}

data class FirestoreData(
    var message: String? = null,
    var image: FirestoreImageData? = null
) {

    fun toMap() = hashMapOf(
        "message" to message
        //"imageModel" to imageModel?.toMap()
    )
}

data class FirestoreImageData(
    var width: Int? = null,
    var height: Int? = null,
    var original: String? = null

) {

    fun toMap() = hashMapOf(
        "width" to width,
        "height" to height,
        "original" to original
    )
}

data class FirestoreUser(
    var id: String? = null,
    var name: String? = null,
    @get:PropertyName("image_url") @set:PropertyName("image_url") var imageUrl: String? = null
) {

    companion object {

        fun user1() = FirestoreUser(
            "user-1",
            "John",
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"
        )

        fun user2() = FirestoreUser(
            "user-2",
            "Camila",
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"
        )
    }

}
