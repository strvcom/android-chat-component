package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.model.ConversationModel

interface ConversationClient {

    fun subscribeConversations(userId: String): ObservableTask<List<ConversationModel>, Throwable>

}