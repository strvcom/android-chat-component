package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.ObservableTask
import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.model.IConversationModel

interface ConversationClient {

    fun createConversation(memberIds: List<String>): Task<String, Throwable>

    fun subscribeConversations(memberId: String): ObservableTask<List<IConversationModel>, Throwable>

}