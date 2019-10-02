package com.strv.chat.firestore.source

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.ListSource
import com.strv.chat.core.domain.observableTask
import com.strv.chat.core.domain.task

internal data class FirestoreListSource<Entity : SourceEntity>(
    private val source: Query,
    private val clazz: Class<Entity>
) : ListSource<Entity> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun get() =
        task<List<Entity>, Throwable> {
            source.get()
                .addOnSuccessListener { result ->
                    val list = arrayListOf<Entity>()

                    result?.mapTo(list) { snapShot ->
                        snapShot.toObject(clazz).also { item ->
                            item.id = snapShot.id
                        }
                    }

                    invokeSuccess(list)
                }

                .addOnFailureListener(this::invokeError)

        }

    override fun subscribe() =
        observableTask<List<Entity>, Throwable>(::unsubscribe) {
            listenerRegistration =
                source.addSnapshotListener { result, exception ->
                    if (exception != null) {
                        invokeError(exception)
                    } else {
                        val list = arrayListOf<Entity>()

                        result?.mapTo(list) { snapShot ->
                            snapShot.toObject(clazz).also { item ->
                                item.id = snapShot.id
                            }
                        }

                        invokeNext(list)
                    }
                }
        }

    override fun unsubscribe() {
        listenerRegistration?.remove()
        listenerRegistration == null
    }
}
