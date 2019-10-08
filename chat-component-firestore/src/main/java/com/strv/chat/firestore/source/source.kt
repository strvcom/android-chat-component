package com.strv.chat.firestore.source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.DocumentSource
import com.strv.chat.core.domain.observableTask
import com.strv.chat.core.domain.task

internal data class FirestoreDocumentSource<Entity : SourceEntity>(
    private val documentReference: DocumentReference,
    private val clazz: Class<Entity>
) : DocumentSource<Entity> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun get() =
        task<Entity, Throwable>() {

            documentReference.get()
                .addOnSuccessListener { snapshot ->

                    val result = snapshot.toObject(clazz).also { item ->
                        item?.id = snapshot.id
                    }

                    invokeSuccess(requireNotNull(result) { "Document ${documentReference.id} doesn't exist" })
                }

                .addOnFailureListener { error ->
                    invokeError(error)
                }
        }

    override fun subscribe() =
        observableTask<Entity, Throwable>(::unsubscribe) {
            listenerRegistration = documentReference.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    invokeError(exception)
                } else {

                    val result = snapshot?.toObject(clazz).also { item ->
                        item?.id = snapshot?.id
                    }

                    invokeNext(requireNotNull(result) { "Document ${documentReference.id} doesn't exist" })
                }
            }
        }

    override fun unsubscribe() {
        listenerRegistration?.remove()
        listenerRegistration == null
    }
}