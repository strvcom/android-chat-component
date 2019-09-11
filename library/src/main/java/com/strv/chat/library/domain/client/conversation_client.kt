package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.ConversationModel

interface ConversationClient {

    fun subscribeConversations(observer: Observer<List<ConversationModel>>)

    fun unsubscribeConversations()
}