package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.MessageModel

interface ChatClient {

    fun sendMessage(message: MessageModel, observer: Observer<Void?>)

    fun subscribeMessages(observer: Observer<List<MessageModel>>, limit: Long = 50)

    fun unsubscribeMessages()
}