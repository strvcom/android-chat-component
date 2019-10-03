package com.strv.chat.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.strv.chat.core.data.entity.SourceEntity
import com.strv.chat.core.data.source.ListSource
import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.observableTask

data class FirebaseListSource<Entity : SourceEntity>(
    private val source: DatabaseReference,
    private val clazz: Class<Entity>
) : ListSource<Entity> {

    private var eventListener: ValueEventListener? = null

    override fun get(): Task<List<Entity>, Throwable> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe() =
        observableTask<List<Entity>, Throwable>(::unsubscribe) {
            eventListener = object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    invokeError(p0.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = arrayListOf<Entity>()

                    dataSnapshot.children.forEach { snapshot ->
                        snapshot.getValue(clazz)?.let { result ->
                            result.id = snapshot.key
                            list.add(result)
                        }
                    }

                    invokeNext(list)
                }
            }
        }

    override fun unsubscribe() {
        eventListener?.let(source::removeEventListener)
        eventListener = null
    }
}