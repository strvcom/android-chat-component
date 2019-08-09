package com.strv.chat.library.data

import com.strv.chat.library.common.ObservableComponent
import com.strv.chat.library.data.model.SourceEntity
import com.strv.chat.library.data.model.Mapper
import com.strv.chat.library.domain.model.ChatItem
import com.strv.chat.library.domain.ChatClient
import com.strv.chat.library.domain.ListSource

class ChatClientImpl<Entity : SourceEntity>(
    private val source: ListSource<Entity>,
    private val chatMapper: Mapper<Entity, ChatItem>
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