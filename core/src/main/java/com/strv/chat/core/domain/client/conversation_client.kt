package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.model.IConversationModel
import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.task.ObservableTask
import com.strv.chat.core.domain.task.Task

/**
 * Provides interactions with the conversation source of data.
 */
interface ConversationClient {

    /**
     * Creates a conversation.
     *
     * @param members a list of members of the conversation.
     *
     * @return [Task] with conversationId in case of success.
     */
    fun createConversation(members: List<IMemberModel>): Task<String, Throwable>

    /**
     * Updates member meta.
     *
     * @param memberMeta meta data to update.
     *
     * @return [Task] with memberId in case of success.
     */
    fun updateMemberMeta(memberMeta: IMemberMetaModel): Task<String, Throwable>

    /**
     * Get conversations of a user.
     *
     * @param memberId id of the user.
     *
     * @return [Task] with List<[IConversationModel]> in case of success.
     */
    fun conversations(memberId: String): Task<List<IConversationModel>, Throwable>

    /**
     * Starts listening to the conversations of a user.
     *
     * @param memberId id of the user.
     *
     * @return [Task] with List<[IConversationModel]> in case of success.
     */
    fun subscribeConversations(memberId: String): ObservableTask<List<IConversationModel>, Throwable>
}