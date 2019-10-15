package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.ObservableTask
import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IMemberModel

interface ConversationClient {

    fun createConversation(members: List<IMemberModel>): Task<String, Throwable>

    fun subscribeConversations(memberId: String): ObservableTask<List<IConversationModel>, Throwable>

}