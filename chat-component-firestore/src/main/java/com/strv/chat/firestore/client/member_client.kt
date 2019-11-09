package com.strv.chat.firestore.client

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task.map
import com.strv.chat.core.domain.task.task
import com.strv.chat.firestore.USERS_COLLECTION
import com.strv.chat.firestore.entity.FirestoreMemberEntity
import com.strv.chat.firestore.entity.creator.MemberEntityConfiguration
import com.strv.chat.firestore.entity.creator.MemberEntityCreator
import com.strv.chat.firestore.firestoreMember
import com.strv.chat.firestore.model.creator.MemberModelConfiguration
import com.strv.chat.firestore.model.creator.MemberModelCreator
import com.strv.chat.firestore.model.creator.MemberModelsConfiguration
import com.strv.chat.firestore.model.creator.MemberModelsCreator
import com.strv.chat.firestore.source

internal class FirestoreMemberClient(
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

    override fun updateMember(
        memberModel: IMemberModel
    ): Task<String, Throwable> = task {
        firestoreMember(firebaseDb, memberModel.memberId)
            .update(MemberEntityCreator.create(MemberEntityConfiguration(memberModel)).toMap())
            .addOnSuccessListener {
                invokeSuccess(memberModel.memberId)
            }.addOnFailureListener { error ->
                invokeError(error)
            }
    }

    //private methods
    private fun firestoreDocumentSource(documentReference: DocumentReference) =
        documentReference.source<FirestoreMemberEntity>()
}