package com.strv.chat.library.business

import com.strv.chat.library.business.common.Observable
import com.strv.chat.library.business.common.ObservableComponent
import com.strv.chat.library.business.data.SourceEntity
import com.strv.chat.library.business.mapper.Mapper
import com.strv.chat.library.chat.domain.ChatItem

/*
    TODO
    The project has dependencies on both - Firestore and Firebase +- Retrofit (btw do we really need it)???
    Is it possible to add dependencies based on the current implementation? Multimodule? Artifacts?
 */

interface ChatClient : Observable<ChatClient.Listener> {

    interface Listener {
        fun onMessagesChanged(chatItems: List<ChatItem>)
        fun onMessagesFetchFailed(exception: Throwable)
    }

    fun getMessages()

    fun sendMessage()
}

class ChatClientImpl<Entity : SourceEntity>(
    private val source: ListSource<Entity>,
    private val chatMapper: Mapper<ChatItem, Entity>
) : ChatClient, ObservableComponent<ChatClient.Listener>() {

    override fun sendMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMessages() {
        source.get(
            onSuccess = { list ->
                notify { onMessagesChanged(list.map(chatMapper::mapToDomain)) }
            },
            onError = { error ->
                notify { onMessagesFetchFailed(error) }
            }
        )
    }

    override fun unregisterListener(listener: ChatClient.Listener) {
        super.unregisterListener(listener)
        source.remove()
    }
}