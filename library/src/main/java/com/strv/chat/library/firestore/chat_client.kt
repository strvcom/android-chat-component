package com.strv.chat.library.firestore

import  com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.data.source.observers.ListSourceObserver
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.MessageId
import com.strv.chat.library.domain.client.ChatObserver
import com.strv.chat.library.firestore.entity.FirestoreMessage
import com.strv.chat.library.firestore.mapper.MessageMapper
import strv.ktools.logE
import java.util.*

class FirestoreChatClient(
    val firebaseDb: FirebaseFirestore,
    val conversationId: String
) : ChatClient {

    private val observableSnapshots = LinkedList<ListSource<out SourceEntity>>()

    override fun messages(limit: Long, startAfter: MessageId, observer: ChatObserver) {
        //todo
    }

    override fun subscribeMessages(limit: Long, observer: ChatObserver) {
        firestoreListSource(firestoreChatMessages(firebaseDb, conversationId))
            .subscribe(object : ListSourceObserver<FirestoreMessage> {

                override fun onSuccess(list: List<FirestoreMessage>) {
                    observer.onNext(list.map(MessageMapper::mapToDomain))
                }

                override fun onError(error: Throwable) {
                    observer.onError(error)
                }
            }).also {
                observableSnapshots.add(it)
            }
    }

    override fun sendMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribeMessages() {
        while (observableSnapshots.isNotEmpty()) {
            observableSnapshots.pop().unsubscribe()
        }
    }

    private fun firestoreListSource(source: Query) =
        source.listSource<FirestoreMessage>()

    data class Builder(
        var firebaseDb: FirebaseFirestore? = null,
        var conversationId: String? = null
    ) {

        fun build() =
            FirestoreChatClient(
                requireNotNull(firebaseDb) { logE("firebaseDb must be specified") },
                requireNotNull(conversationId) { logE("conversationId must be specified") }
            )
    }
}