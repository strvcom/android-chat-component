package com.strv.chat.library.business

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.Query
import com.strv.chat.library.business.common.Observable
import com.strv.chat.library.business.common.ObservableComponent
import com.strv.chat.library.chat.domain.ChatItem
import retrofit2.Call
import java.lang.Exception

/*
    The project has dependencies on both - Firestore and Firebase +- Retrofit (btw do we really need it)???
    Is it possible to add dependencies based on the current implementation?
 */

interface ChatClient<S> : Observable<ChatClient.Listener> {

    interface Listener {
        fun onMessagesChanged(chatItems: List<ChatItem>)
        fun onMessagesFetchFailed(exception: Exception)
    }

    fun <T> getMessages(source: S, clazz: Class<T>, mapper: (T) -> ChatItem)
//
    fun <T> getRealtimeMessages(source: S, clazz: Class<T>, mapper: (T) -> ChatItem)
}


class FirebaseChatClient(

) : ChatClient<DatabaseReference>, ObservableComponent<ChatClient.Listener>() {

    override fun <T> getMessages(source: DatabaseReference, clazz: Class<T>, mapper: (T) -> ChatItem) {
        source.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //todo update
                notify { onMessagesFetchFailed(Exception(p0.message)) }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = arrayListOf<T>()

                dataSnapshot.children.forEach { postSnapshot ->
                    postSnapshot.getValue(clazz)?.let {
                        list.add(it)
                        //todo what about id?
                    }
                }

                notify { onMessagesChanged(list.map(mapper)) }
            }

        })
    }

    override fun <T> getRealtimeMessages(source: DatabaseReference, clazz: Class<T>, mapper: (T) -> ChatItem) {
        source.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                //todo update, create error wrapper
                notify { onMessagesFetchFailed(Exception(p0.message)) }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = arrayListOf<T>()

                dataSnapshot.children.forEach { postSnapshot ->
                    postSnapshot.getValue(clazz)?.let {
                        list.add(it)
                        //todo what about id?
                    }
                }

                notify { onMessagesChanged(list.map(mapper)) }
            }

        })
    }
}

class FirestoreChatClient() : ChatClient<Query>, ObservableComponent<ChatClient.Listener>() {

    override fun <T> getMessages(source: Query, clazz: Class<T>, mapper: (T) -> ChatItem) {
        source.get()
            .addOnSuccessListener { result ->
                val list = arrayListOf<T>()

                result.mapTo(list) { item ->
                    item.toObject(clazz)
                }

                notify { onMessagesChanged(list.map(mapper)) }
            }
            .addOnFailureListener { exception ->
                notify { onMessagesFetchFailed(exception) }
            }
    }

    override fun <T> getRealtimeMessages(source: Query, clazz: Class<T>, mapper: (T) -> ChatItem) {
        source.addSnapshotListener { result, exception ->
            if (exception != null) {
                notify { onMessagesFetchFailed(exception) }
            } else {
                val list = arrayListOf<T>()

                result?.mapTo(list) { item ->
                    item.toObject(clazz)
                }

                notify { onMessagesChanged(list.map(mapper)) }
            }
        }
    }
}
