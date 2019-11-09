package com.strv.chat.core.domain.model

/**
 * Represents a structure of a member of a conversation.
 */
interface IMemberModel {

    /**
     * Id of the member.
     */
    val memberId: String

    /**
     * Name of the member.
     */
    val memberName: String

    /**
     * Image url of the user's profile picture.
     */
    val memberPhotoUrl: String
}

/**
 * Represents a structure of the minimum necessary info about the members of the conversation.
 */
interface IMemberMetaModel {

    /**
     * Id of the member.
     */
    val memberId: String

    /**
     * Name of the member.
     */
    val memberName: String
}