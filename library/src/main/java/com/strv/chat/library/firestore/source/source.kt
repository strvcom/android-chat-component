package com.strv.chat.library.firestore.source

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.data.source.Source
import com.strv.chat.library.data.source.observer.SourceObserver

internal data class FirestoreSource<Entity : SourceEntity>(
    private val source: Query,
    private val clazz: Class<Entity>
) : Source<Entity> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun get(observer: SourceObserver<Entity>) {
        source.get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<Entity>()

                result?.mapTo(list) { snapShot ->
                    snapShot.toObject(clazz).also { item ->
                        item.id = snapShot.id
                    }
                }

                observer.onSuccess(list.firstOrNull())
            }

            .addOnFailureListener { observer.onError(it) }

    }

    override fun subscribe(observer: SourceObserver<Entity>) = apply {
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

                observer.onSuccess(list.firstOrNull())
            }
        }
    }

    override fun unsubscribe() {
        listenerRegistration?.remove()
    }
}