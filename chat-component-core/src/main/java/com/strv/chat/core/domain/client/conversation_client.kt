package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.ObservableTask
import com.strv.chat.core.domain.model.IConversationModel

interface ConversationClient {

    fun subscribeConversations(userId: String): ObservableTask<List<IConversationModel>, Throwable>

}