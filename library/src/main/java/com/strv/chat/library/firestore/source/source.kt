package com.strv.chat.library.firestore.source

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.Source
import com.strv.chat.library.domain.observableTask
import com.strv.chat.library.domain.task

internal data class FirestoreSource<Entity : SourceEntity>(
    private val source: Query,
    private val clazz: Class<Entity>
) : Source<Entity> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun get() =
        task<Entity?, Throwable>() {
            source.get()
                .addOnSuccessListener { result ->
                    val list = arrayListOf<Entity>()

                    result?.mapTo(list) { snapShot ->
                        snapShot.toObject(clazz).also { item ->
                            item.id = snapShot.id
                        }
                    }

                    invokeSuccess(list.firstOrNull())
                }

                .addOnFailureListener { error ->
                    invokeError(error)
                }
        }

    override fun subscribe() =
        observableTask<Entity?, Throwable>(::unsubscribe) {
            listenerRegistration = source.addSnapshotListener { result, exception ->
                if (exception != null) {
                    invokeError(exception)
                } else {
                    val list = arrayListOf<Entity>()

                    result?.mapTo(list) { snapShot ->
                        snapShot.toObject(clazz).also { item ->
                            item.id = snapShot.id
                        }
                    }

                    invokeNext(list.firstOrNull())
                }
            }
        }

    override fun unsubscribe() {
        listenerRegistration?.remove()
        listenerRegistration == null
    }
}