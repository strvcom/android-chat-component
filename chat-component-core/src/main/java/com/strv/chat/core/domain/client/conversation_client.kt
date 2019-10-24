package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.IMemberModel

interface ConversationClient {

    fun createConversation(members: List<IMemberModel>): Task<String, Throwable>

    fun updateMemberMeta(memberMeta: IMemberMetaModel): Task<String, Throwable>

    fun subscribeConversations(memberId: String): ObservableTask<List<IConversationModel>, Throwable>

    fun conversations(memberId: String): Task<List<IConversationModel>, Throwable>
}