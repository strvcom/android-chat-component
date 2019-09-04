package com.strv.chat.library.firestore.dataSource

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.dataSource.ListSource
import com.strv.chat.library.data.dataSource.ListSourceObserver

internal data class FirestoreListSource<Entity : SourceEntity>(
    private val source: Query,
    private val clazz: Class<Entity>
) : ListSource<Entity> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun get(observer: ListSourceObserver<Entity>) {
        source.get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Entity>()

                result?.mapTo(list) { snapShot ->
                    snapShot.toObject(clazz).also { item ->
                        item.id = snapShot.id
                    }
                }

                observer.onSuccess(list)
            }

            .addOnFailureListener { observer.onError(it) }

    }

    override fun subscribe(observer: ListSourceObserver<Entity>) = apply {
        listenerRegistration = source.addSnapshotListener { result, exception ->
            if (exception != null) {
                observer.onError(exception)
            } else {
                val list = arrayListOf<Entity>()

                result?.mapTo(list) { snapShot ->
                    snapShot.toObject(clazz).also { item ->
                        item.id = snapShot.id
                    }
                }

                observer.onSuccess(list)
            }
        }
    }

    override fun unsubscribe() {
        listenerRegistration?.remove()
    }
}
