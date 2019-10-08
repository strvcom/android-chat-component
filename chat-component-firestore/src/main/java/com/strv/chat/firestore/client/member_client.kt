package com.strv.chat.firestore.client

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.map
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task
import com.strv.chat.firestore.USERS_COLLECTION
import com.strv.chat.firestore.entity.FirestoreMemberEntity
import com.strv.chat.firestore.firestoreMember
import com.strv.chat.firestore.model.creator.MemberModelConfiguration
import com.strv.chat.firestore.model.creator.MemberModelCreator
import com.strv.chat.firestore.model.creator.MemberModelsConfiguration
import com.strv.chat.firestore.model.creator.MemberModelsCreator
import com.strv.chat.firestore.source

class FirestoreMemberClient(
    private val firebaseDb: FirebaseFirestore,
    private val currentUserId: String
) : MemberClient {

    override fun currentUserId(): String =
        currentUserId

    override fun member(memberId: String): Task<IMemberModel, Throwable> =
        firestoreDocumentSource(firestoreMember(firebaseDb, memberId))
            .get()
            .map { entity ->
                MemberModelCreator.create(MemberModelConfiguration(requireNotNull(entity)))
            }

    //todo how to handle title + picture of the conversation?
    //it is not possible to fetch multiple documents at the same time - with a transaction is possible but
    //!! every document read in a transaction must also be written !!
    //one solution would be to have a picture and a name of the conversation dirrectly in the structure
    override fun members(memberIds: List<String>): Task<List<IMemberModel>, Throwable> =
        task<List<FirestoreMemberEntity>, Throwable> {
            firebaseDb.runTransaction { transaction ->
                val members = mutableListOf<FirestoreMemberEntity>()

                for (memberId in memberIds) {
                    val memberDocument = firebaseDb.collection(USERS_COLLECTION).document(memberId)
                    val memberEntity =
                        transaction.get(memberDocument).toObject(FirestoreMemberEntity::class.java)

                    if (memberEntity != null) {
                        transaction.update(memberDocument, memberEntity.toMap())
                        members.add(memberEntity)
                    }
                }

                members.toList()
            }.addOnSuccessListener { members ->
                invokeSuccess(members)
            }.addOnFailureListener { error ->
                invokeError(error)
            }
        }.map { memberEntityList ->
            MemberModelsCreator.create(MemberModelsConfiguration(memberEntityList))
        }

    private fun firestoreDocumentSource(documentReference: DocumentReference) =
        documentReference.source<FirestoreMemberEntity>()
}