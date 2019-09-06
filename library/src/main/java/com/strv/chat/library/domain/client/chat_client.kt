package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.client.observer.ClientObserver

interface ChatClient {

    fun sendMessage()

    fun subscribeMessages(limit: Long = 50, observer: ClientObserver)

    fun unsubscribeMessages()
}