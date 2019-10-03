package com.strv.chat.firestore.client

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.map
import com.strv.chat.core.domain.task
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.firestore.CONVERSATIONS_COLLECTION
import com.strv.chat.firestore.MESSAGES_COLLECTION
import com.strv.chat.firestore.SEEN_COLLECTION
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.LAST_MESSAGE
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

            firebaseDb.batch()
                .run {
                    set(
                        messageDocument,
                        MessageEntityCreator.create(
                            MessageEntityConfiguration(
                                messageDocument.id,
                                message
                            )
                        ).toMap()
                    )
                    update(
                        conversationDocument,
                        LAST_MESSAGE,
                        MessageEntityCreator.create(
                            MessageEntityConfiguration(
                                messageDocument.id,
                                message
                            )
                        ).toLastMessageMap()
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

            seenSenderDocument.set(SeenEntityCreator.create(SeenEntityConfiguration(model)).toMap())
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