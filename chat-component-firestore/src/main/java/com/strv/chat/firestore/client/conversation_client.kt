package com.strv.chat.firestore.client

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.map
import com.strv.chat.firestore.entity.FirestoreConversationEntity
import com.strv.chat.firestore.firestoreConversations
import com.strv.chat.firestore.listSource
import com.strv.chat.firestore.model.creator.ConversationModelListConfiguration
import com.strv.chat.firestore.model.creator.ConversationModelListCreator

class FirestoreConversationClient(
    val firebaseDb: FirebaseFirestore
) : ConversationClient {

    override fun subscribeConversations(userId: String) =
        firestoreListSource(firestoreConversations(firebaseDb, userId))
            .subscribe()
            .map { conversation ->
                ConversationModelListCreator.create(ConversationModelListConfiguration(conversation))
            }


    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreConversationEntity>()
}