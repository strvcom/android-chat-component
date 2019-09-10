package com.strv.chat.library.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.mapper.messageEntity
import com.strv.chat.library.firestore.mapper.messageModels
import com.strv.chat.library.firestore.mapper.seenEntity
import strv.ktools.logE
import strv.ktools.logI
import java.util.LinkedList

class FirestoreChatClient(
    val firebaseDb: FirebaseFirestore,
    val conversationId: String
) : ChatClient {

    private val observableSnapshots = LinkedList<ListSource<out SourceEntity>>()

    override fun subscribeMessages(observer: Observer<List<MessageModelResponse>>, limit: Long) {
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId))
            .subscribe(observer.convert(::messageModels))
            .also {
                observableSnapshots.add(it)
            }
    }

    override fun sendMessage(message: MessageModelRequest, observer: Observer<Void?>) {
        val conversationDocument =
            firebaseDb.collection(CONVERSATIONS_COLLECTION).document(conversationId)
        val messageDocument = conversationDocument.collection(MESSAGES_COLLECTION).document()

        firebaseDb.batch().run {
            set(messageDocument, messageEntity(message).toMap())
            //todo replace with a constant
            update(conversationDocument, "last_message", messageEntity(message).toMap())
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

    override fun setSeen(userId: String, model: MessageModelResponse) {
        val seenSenderDocument = firebaseDb
            .collection(CONVERSATIONS_COLLECTION)
            .document(conversationId)
            .collection(SEEN_COLLECTION)
            .document(userId)

        seenSenderDocument.set(seenEntity(model).toMap())
            .addOnSuccessListener { logI("Message ${model.id} has been marked as seen") }
            .addOnFailureListener { logE(it.localizedMessage) }
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