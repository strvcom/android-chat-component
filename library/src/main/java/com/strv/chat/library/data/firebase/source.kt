package com.strv.chat.library.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.strv.chat.library.data.model.SourceEntity
import com.strv.chat.library.domain.ListSource

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