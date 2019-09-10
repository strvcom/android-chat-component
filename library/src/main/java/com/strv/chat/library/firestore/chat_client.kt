package com.strv.chat.library.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.model.MessageModel
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.mapper.messageEntity
import com.strv.chat.library.firestore.mapper.messageModels
import strv.ktools.logE
import java.util.LinkedList

class FirestoreChatClient(
    val firebaseDb: FirebaseFirestore,
    val conversationId: String
) : ChatClient {

    private val observableSnapshots = LinkedList<ListSource<out SourceEntity>>()

    override fun subscribeMessages(observer: Observer<List<MessageModel>>, limit: Long) {
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId))
            .subscribe(observer.convert(::messageModels))
            .also {
                observableSnapshots.add(it)
            }
    }

    override fun sendMessage(message: MessageModel, observer: Observer<Void?>) {
        val conversationDocument =
            firebaseDb.collection(CONVERSATIONS_COLLECTION).document(conversationId)
        val messageDocument = conversationDocument.collection(MESSAGES_COLLECTION).document()
        val seenSenderDocument =
            conversationDocument.collection(SEEN_COLLECTION).document(message.senderId)

        firebaseDb.batch().run {
            set(messageDocument, messageEntity(message).toMap())
            //todo replace with a constant
            update(conversationDocument, "last_message", messageEntity(message).toMap())
            //todo replace
            set(seenSenderDocument, hashMapOf("message_id" to messageDocument.id))
            commit()
        }
            .addOnSuccessListener(observer::onSuccess)
            .addOnFailureListener(observer::onError)
    }

    override fun unsubscribeMessages() {
        while (observableSnapshots.isNotEmpty()) {
            observableSnapshots.pop().unsubscribe()
        }
    }

    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreMessage>()

    data class Builder(
        var firebaseDb: FirebaseFirestore? = null,
        var conversationId: String? = null
    ) {

        fun build() =
            FirestoreChatClient(
                requireNotNull(firebaseDb) { logE("firebaseDb must be specified") },
                requireNotNull(conversationId) { logE("conversationId must be specified") }
            )
    }
}