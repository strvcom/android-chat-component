package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.MessageInputModel
import java.util.Date

/**
 * Provides interactions with the message source of data.
 */
interface ChatClient {

    /**
     * Sends a message.
     *
     * @param MessageInputModel a message model to send.
     *
     * @return [Task] with message id in case of success.
     */
    fun sendMessage(message: MessageInputModel): Task<String, Throwable>

    /**
     * Mark an unseen message as seen.
     *
     * @param currentUserId id of the current user.
     * @param conversationId id of the conversation that contains the message with [messageId].
     * @param messageId id of the message that is supposed to be marked as seen if it is not.
     *
     * @return [Task] with [messageId] in case of success.
     */
    fun setSeenIfNot(currentUserId: String, conversationId: String, messageId: String): Task<String, Throwable>

    /**
     * Get messages of a conversation.
     *
     * @param conversationId id of the conversation.
     * @param startAfter offset the result to the specified date.
     * @param limit limits the result to the specified number of items.
     *
     * @return [Task] with List<[IMessageModel]> in case of success.
     */
    fun messages(conversationId: String, startAfter: Date, limit: Long = 50): Task<List<IMessageModel>, Throwable>

    /**
     * Starts listening to the first [limit] messages of a conversation.
     *
     * @param conversationId id of the conversation.
     * @param limit limits the result to the specified number of items.
     *
     * @return [Task] with List<[IMessageModel]> in case of success.
     */
    fun subscribeMessages(conversationId: String, limit: Long = 50): ObservableTask<List<IMessageModel>, Throwable>
}