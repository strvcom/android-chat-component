package com.strv.chat.library.domain.client

inline class MessageId(val messageId: String)

interface ChatClient {

    fun messages(limit: Long = 50, startAfter: MessageId, observer: ChatObserver)

    fun sendMessage()

    fun subscribeMessages(limit: Long = 50, observer: ChatObserver)

    fun unsubscribeMessages()
}