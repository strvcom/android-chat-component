package com.strv.chat.library.firestore.client

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.domain.client.ConversationClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.client.observer.convert
import com.strv.chat.library.domain.model.ConversationModel
import com.strv.chat.library.firestore.entity.FirestoreConversation
import com.strv.chat.library.firestore.firestoreConversations
import com.strv.chat.library.firestore.listSource
import com.strv.chat.library.firestore.mapper.conversationModels
import strv.ktools.logE
import java.util.LinkedList

class FirestoreConversationClient(
    val firebaseDb: FirebaseFirestore,
    val userId: String
): ConversationClient {

    private val observableSnapshots = LinkedList<ListSource<out SourceEntity>>()

    override fun subscribeConversations(observer: Observer<List<ConversationModel>>) {
        firestoreListSource(
            firestoreConversations(
                firebaseDb,
                userId
            )
        )
            .subscribe(observer.convert(::conversationModels))
            .also {
                observableSnapshots.add(it)
            }
    }

    override fun unsubscribeConversations() {
        while (observableSnapshots.isNotEmpty()) {
            observableSnapshots.pop().unsubscribe()
        }
    }

    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreConversation>()

    data class Builder(
        var firebaseDb: FirebaseFirestore? = null,
        var userId: String? = null
    ) {

        fun build() =
            FirestoreConversationClient(
                requireNotNull(firebaseDb) { logE("firebaseDb must be specified") },
                requireNotNull(userId) { logE("userId must be specified") }
            )
    }
}