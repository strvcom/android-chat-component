package com.strv.chat.library.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.strv.chat.library.data.source.ListSource
import com.strv.chat.library.data.entity.SourceEntity
import com.strv.chat.library.domain.client.observer.Observer

data class FirebaseListSource<T : SourceEntity>(
    private val source: DatabaseReference,
    private val clazz: Class<T>
) : ListSource<T> {

    private lateinit var eventListener: ValueEventListener

    override fun get(observer: Observer<List<T>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(observer: Observer<List<T>>): ListSource<T> =
        apply {
            eventListener = object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    observer.onError(p0.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = arrayListOf<T>()

                dataSnapshot.children.forEach { snapshot ->
                    snapshot.getValue(clazz)?.let { result ->
                        result.id = snapshot.key
                        list.add(result)
                    }
                }

                observer.onSuccess(list)
            }
        }

            source.addValueEventListener(eventListener)
        }

    override fun unsubscribe() {
        source.removeEventListener(eventListener)
    }
}