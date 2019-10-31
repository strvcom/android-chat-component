package com.strv.chat.firestore.client

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task.flatMap
import com.strv.chat.core.domain.task.map
import com.strv.chat.core.domain.task.task
import com.strv.chat.firestore.CONVERSATIONS_COLLECTION
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.entity.MEMBERS_META
import com.strv.chat.firestore.entity.creator.MemberMetaEntityConfiguration
import com.strv.chat.firestore.entity.creator.MemberMetaEntityCreator
import com.strv.chat.firestore.firestoreConversation
import com.strv.chat.firestore.firestoreConversations
import com.strv.chat.firestore.listSource
import com.strv.chat.firestore.model.creator.ConversationModelListConfiguration
import com.strv.chat.firestore.model.creator.ConversationModelListCreator

internal class FirestoreConversationClient(
    val firebaseDb: FirebaseFirestore
) : ConversationClient {

    override fun createConversation(members: List<IMemberModel>): Task<String, Throwable> =
        task {
            val conversationDocument =
                firebaseDb.collection(CONVERSATIONS_COLLECTION).document()

            conversationDocument.set(
                FirestoreConversationEntity(
                    members = members.map { member -> member.memberId },
                    membersMeta = members.map { member ->
                        member.memberId to MemberMetaEntityCreator.create(
                            MemberMetaEntityConfiguration(member.memberName)
                        )
                    }.toMap(),
                    seen = members.map { member -> member.memberId to null }.toMap()
                ).toMap()
            ).addOnSuccessListener {
                invokeSuccess(conversationDocument.id)

            }.addOnFailureListener { error ->
                invokeError(error)
            }
        }

    override fun updateMemberMeta(memberMeta: IMemberMetaModel): Task<String, Throwable> =
        conversations(memberMeta.memberId)
            .flatMap { models ->
                task<String, Throwable> {
                    firebaseDb.runTransaction { transaction ->

                        models.forEach { model ->
                            transaction.update(
                                firestoreConversation(firebaseDb, model.id),
                                "$MEMBERS_META.${memberMeta.memberId}",
                                MemberMetaEntityCreator.create(
                                    MemberMetaEntityConfiguration(
                                        memberMeta.memberName
                                    )
                                ).toMap()
                            )
                        }

                        memberMeta.memberId
                    }.addOnSuccessListener { memberId ->
                        invokeSuccess(memberId)
                    }.addOnFailureListener { error ->
                        invokeError(error)
                    }
                }
            }

    override fun subscribeConversations(memberId: String) =
        firestoreListSource(firestoreConversations(firebaseDb, memberId))
            .subscribe()
            .map { conversation ->
                ConversationModelListCreator.create(ConversationModelListConfiguration(conversation))
            }

    override fun conversations(memberId: String) =
        firestoreListSource(firestoreConversations(firebaseDb, memberId))
            .get()
            .map { conversation ->
                ConversationModelListCreator.create(ConversationModelListConfiguration(conversation))
            }

    //private methods
    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreConversationEntity>()
}