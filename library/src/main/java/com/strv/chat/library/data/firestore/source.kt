package com.strv.chat.library.data.firestore

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.model.SourceEntity
import com.strv.chat.library.domain.ListSource

//todo maybe add some internal flag/anotation whatever
data class FirestoreListSource<T : SourceEntity>(
    private val source: Query,
    private val clazz: Class<T>
) : ListSource<T> {

    private lateinit var listenerRegistration: ListenerRegistration

    override fun get(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit) {
        listenerRegistration = source.addSnapshotListener { result, exception ->
            if (exception != null) {
                onError(exception)
            } else {
                val list = arrayListOf<T>()

                result?.mapTo(list) { snapShot ->
                    snapShot.toObject(clazz).also { item ->
                        item.key = snapShot.id
                    }
                }

                onSuccess(list)
            }
        }
    }

    override fun remove() {
        listenerRegistration.remove()
    }
}
