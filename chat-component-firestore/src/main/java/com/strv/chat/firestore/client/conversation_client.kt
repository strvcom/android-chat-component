package com.strv.chat.firestore.client

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.map
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task
import com.strv.chat.firestore.CONVERSATIONS_COLLECTION
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.firestoreConversations
import com.strv.chat.firestore.listSource
import com.strv.chat.firestore.model.creator.ConversationModelListConfiguration
import com.strv.chat.firestore.model.creator.ConversationModelListCreator

class FirestoreConversationClient(
    val firebaseDb: FirebaseFirestore
) : ConversationClient {

    override fun createConversation(members: List<IMemberModel>): Task<String, Throwable> =
        task {
            val conversationDocument =
                firebaseDb.collection(CONVERSATIONS_COLLECTION).document()

            conversationDocument.set(
                FirestoreConversationEntity(
                    members = members.map { member -> member.memberId },
                    membersMeta = members.map { member -> member.memberId to member.memberName }.toMap(),
                    seen = members.map { member -> member.memberId to null }.toMap()
                ).toMap()
            ).addOnSuccessListener {
                invokeSuccess(conversationDocument.id)

            }.addOnFailureListener { error ->
                invokeError(error)
            }
        }

    override fun subscribeConversations(memberId: String) =
        firestoreListSource(firestoreConversations(firebaseDb, memberId))
            .subscribe()
            .map { conversation ->
                ConversationModelListCreator.create(ConversationModelListConfiguration(conversation))
            }


    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreConversationEntity>()
}