package com.strv.chat.library.firestore.client

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.map
import com.strv.chat.library.domain.task
import com.strv.chat.library.domain.model.IMessageModel
import com.strv.chat.library.domain.model.MessageInputModel
import com.strv.chat.library.firestore.CONVERSATIONS_COLLECTION
import com.strv.chat.library.firestore.MESSAGES_COLLECTION
import com.strv.chat.library.firestore.SEEN_COLLECTION
import com.strv.chat.library.firestore.entity.FirestoreMessageEntity
import com.strv.chat.library.firestore.entity.LAST_MESSAGE
import com.strv.chat.library.firestore.firestoreChatMessages
import com.strv.chat.library.firestore.listSource
import com.strv.chat.library.firestore.model.mapper.messageEntity
import com.strv.chat.library.firestore.model.mapper.messageModels
import com.strv.chat.library.firestore.model.mapper.seenEntity
import java.util.*

class FirestoreChatClient(
    val firebaseDb: FirebaseFirestore
) : ChatClient {

    override fun messages(conversationId: String, startAfter: Date, limit: Long) =
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId, Timestamp(startAfter)))
            .get()
            .map(::messageModels)

    override fun subscribeMessages(
        conversationId: String,
        limit: Long
    ) =
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId))
            .subscribe()
            .map(::messageModels)


    override fun sendMessage(messageInput: MessageInputModel) =
        task<String, Throwable> {
            val conversationDocument =
                firebaseDb.collection(CONVERSATIONS_COLLECTION).document(messageInput.conversationId)
            val messageDocument = conversationDocument.collection(MESSAGES_COLLECTION).document()

            firebaseDb.batch()
                .run {
                    set(messageDocument, messageEntity(messageDocument.id, messageInput).toMap())
                    update(
                        conversationDocument,
                        LAST_MESSAGE,
                        messageEntity(messageDocument.id, messageInput).toLastMessageMap()
                    )
                    commit()
                }
                .addOnSuccessListener {
                    invokeSuccess(messageDocument.id)
                }
                .addOnFailureListener { error ->
                    invokeError(error)
                }
        }

    override fun setSeen(userId: String, conversationId: String, model: IMessageModel) =
        task<String, Throwable> {
            val seenSenderDocument = firebaseDb
                .collection(CONVERSATIONS_COLLECTION)
                .document(conversationId)
                .collection(SEEN_COLLECTION)
                .document(userId)

            seenSenderDocument.set(seenEntity(model).toMap())
                .addOnSuccessListener {
                    invokeSuccess(model.id)
                }
                .addOnFailureListener { error ->
                    invokeError(error)
                }
        }

    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreMessageEntity>()
}