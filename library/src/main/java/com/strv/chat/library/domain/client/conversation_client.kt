package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.model.IConversationModel

interface ConversationClient {

    fun subscribeConversations(userId: String): ObservableTask<List<IConversationModel>, Throwable>

}