package com.strv.chat.library.business

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.strv.chat.library.business.data.SourceEntity

interface ListSource<T : SourceEntity> {

    fun get(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit)

    fun remove()
}

//todo maybe add some internal flag/anotation whatever
data class FirebaseListSource<T : SourceEntity>(
    private val source: DatabaseReference,
    private val clazz: Class<T>
) : ListSource<T> {

    private lateinit var eventListener: ValueEventListener

    override fun get(onSuccess: (List<T>) -> Unit, onError: (Throwable) -> Unit) {
        eventListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                onError(p0.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = arrayListOf<T>()

                dataSnapshot.children.forEach { snapshot ->
                    snapshot.getValue(clazz)?.let { result ->
                        result.key = snapshot.key
                        list.add(result)
                    }
                }

                onSuccess(list)
            }
        }

        source.addValueEventListener(eventListener)
    }

    override fun remove() {
        source.removeEventListener(eventListener)
    }
}

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
