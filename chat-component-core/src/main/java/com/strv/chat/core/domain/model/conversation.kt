package com.strv.chat.core.domain.model

import java.util.Date

/**
 * Represents a structure of a conversation.
 */
interface IConversationModel {

    /**
     * An unique identifier of the conversation.
     */
    val id: String

    /**
     * Minimum necessary info about the members of the conversation.
     */
    val members: List<IMemberMetaModel>

    /**
     * A map with userId as an identifier saving the last message that was read.
     */
    val seen: Map<String, ISeenModel?>

    /**
     * Last message of the conversation.
     */
    val lastMessage: IMessageModel
}

/**
 * Represents the minimum necessary info about the last seen message.
 */
interface ISeenModel {

    /**
     * Id of the last seen message.
     */
    val messageId: String

    /**
     * Date of seeing the last message.
     */
    val date: Date
}