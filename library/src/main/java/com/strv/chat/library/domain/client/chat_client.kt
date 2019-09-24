package com.strv.chat.library.domain.client

import com.strv.chat.library.domain.ObservableTask
import com.strv.chat.library.domain.Task
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelResponse
import java.util.*

interface ChatClient {

    fun sendMessage(message: MessageModelRequest): Task<String, Throwable>

    fun setSeen(userId: String, conversationId: String, model: MessageModelResponse): Task<String, Throwable>

    fun messages(conversationId: String, startAfter: Date, limit: Long = 50): Task<List<MessageModelResponse>, Throwable>

    fun subscribeMessages(conversationId: String, limit: Long = 50): ObservableTask<List<MessageModelResponse>, Throwable>
}