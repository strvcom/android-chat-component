package com.strv.chat.library.domain

inline class MessageId(val messageId: String)

interface ChatClient {

    fun messages(limit: Long = 50, startAfter: MessageId, observer: MessagesObserver)

    fun subscribeMessages(limit: Long = 50, observer: MessagesObserver)

    fun sendMessage()

    fun unsubscribeMessages()
}