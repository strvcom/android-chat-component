package com.strv.chat.firestore.client

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.WriteBatch
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.core.domain.task.map
import com.strv.chat.core.domain.task.task
import com.strv.chat.firestore.CONVERSATIONS_COLLECTION
import com.strv.chat.firestore.MESSAGES_COLLECTION
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.LAST_MESSAGE
import com.strv.chat.firestore.entity.SEEN
import com.strv.chat.firestore.entity.creator.MessageEntityConfiguration
import com.strv.chat.firestore.entity.creator.MessageEntityCreator
import com.strv.chat.firestore.entity.creator.SeenEntityConfiguration
import com.strv.chat.firestore.entity.creator.SeenEntityCreator
import com.strv.chat.firestore.firestoreChatMessages
import com.strv.chat.firestore.listSource
import com.strv.chat.firestore.model.creator.MessageModelListConfiguration
import com.strv.chat.firestore.model.creator.MessageModelListCreator
import java.util.Date

class FirestoreChatClient(
    val firebaseDb: FirebaseFirestore
) : ChatClient {

    override fun messages(conversationId: String, startAfter: Date, limit: Long) =
        firestoreListSource(
            firestoreChatMessages(
                firebaseDb,
                conversationId,
                Timestamp(startAfter)
            )
        )
            .get()
            .map { messages ->
                MessageModelListCreator.create(MessageModelListConfiguration(messages))
            }

    override fun subscribeMessages(
        conversationId: String,
        limit: Long
    ) =
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId))
            .subscribe()
            .map { messages ->
                MessageModelListCreator.create(MessageModelListConfiguration(messages))
            }


    override fun sendMessage(message: MessageInputModel) =
        task<String, Throwable> {
            val conversationDocument =
                firebaseDb.collection(CONVERSATIONS_COLLECTION).document(message.conversationId)

            val messageDocument = conversationDocument.collection(MESSAGES_COLLECTION).document()

            val batch = firebaseDb.batch()

            newMessage(batch, messageDocument, message)
            lastMessage(batch, conversationDocument, messageDocument, message)
            seen(batch, conversationDocument, messageDocument.id, message.senderId)

            batch.commit()
                .addOnSuccessListener {
                    invokeSuccess(messageDocument.id)
                }
                .addOnFailureListener { error ->
                    invokeError(error)
                }
        }

    override fun setSeenIfNot(currentUserId: String, conversationId: String, messageId: String) =
        task<String, Throwable> {
            val conversationDocument =
                firebaseDb.collection(CONVERSATIONS_COLLECTION).document(conversationId)

            firebaseDb.runTransaction { transaction ->
                val conversation = transaction.get(conversationDocument)

                if (conversation.getString("$LAST_MESSAGE.$currentUserId") !== messageId) {
                    seen(transaction, conversationDocument, messageId, currentUserId)
                }
            }.addOnSuccessListener {
                invokeSuccess(messageId)
            }.addOnFailureListener { error ->
                invokeError(error)
            }
        }

    private fun newMessage(
        batch: WriteBatch,
        messageDocument: DocumentReference,
        message: MessageInputModel
    ) {
        batch.set(
            messageDocument,
            MessageEntityCreator.create(
                MessageEntityConfiguration(
                    messageDocument.id,
                    message
                )
            ).toMap()
        )
    }

    private fun lastMessage(
        batch: WriteBatch,
        conversationDocument: DocumentReference,
        messageDocument: DocumentReference,
        message: MessageInputModel
    ) {
        batch.update(
            conversationDocument,
            LAST_MESSAGE,
            MessageEntityCreator.create(
                MessageEntityConfiguration(
                    messageDocument.id,
                    message
                )
            ).toLastMessageMap()
        )
    }

    private fun seen(
        batch: WriteBatch,
        conversationDocument: DocumentReference,
        messageId: String,
        senderId: String
    ) {
        batch.update(
            conversationDocument,
            "$SEEN.$senderId",
            SeenEntityCreator.create(
                SeenEntityConfiguration(
                    messageId
                )
            ).toMap()
        )
    }

    private fun seen(
        transaction: Transaction,
        conversationDocument: DocumentReference,
        messageId: String,
        senderId: String
    ) {
        transaction.update(
            conversationDocument,
            "$SEEN.$senderId",
            SeenEntityCreator.create(
                SeenEntityConfiguration(
                    messageId
                )
            ).toMap()
        )
    }

    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreMessageEntity>()
}