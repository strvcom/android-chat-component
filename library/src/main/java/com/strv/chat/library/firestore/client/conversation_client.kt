package com.strv.chat.library.firestore.client

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.map
import com.strv.chat.library.firestore.entity.FirestoreConversation
import com.strv.chat.library.firestore.firestoreConversations
import com.strv.chat.library.firestore.listSource
import com.strv.chat.library.firestore.mapper.conversationModels

class FirestoreConversationClient(
    val firebaseDb: FirebaseFirestore
): ConversationClient {

    override fun subscribeConversations(userId: String) =
        firestoreListSource(firestoreConversations(firebaseDb, userId))
            .subscribe().map(::conversationModels)


    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreConversation>()
}