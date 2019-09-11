package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelResponse

interface ChatClient {

    fun sendMessage(message: MessageModelRequest, observer: Observer<Void?>)

    fun setSeen(userId: String, model: MessageModelResponse)

    fun subscribeMessages(observer: Observer<List<MessageModelResponse>>, limit: Long = 50)

    fun unsubscribeMessages()
}