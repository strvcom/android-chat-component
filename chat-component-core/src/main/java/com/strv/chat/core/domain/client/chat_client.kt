package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.MessageInputModel
import java.util.Date

interface ChatClient {

    fun sendMessage(message: MessageInputModel): Task<String, Throwable>

    fun setSeenIfNot(currentUserId: String, conversationId: String, messageId: String): Task<String, Throwable>

    fun messages(conversationId: String, startAfter: Date, limit: Long = 50): Task<List<IMessageModel>, Throwable>

    fun subscribeMessages(conversationId: String, limit: Long = 50): ObservableTask<List<IMessageModel>, Throwable>
}