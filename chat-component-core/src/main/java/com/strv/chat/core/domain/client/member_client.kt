package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.model.IMemberModel

/**
 * Provides information about the current user and other members.
 */
interface MemberClient {

    /**
     * Returns id of the current user.
     *
     * @return id of the current user.
     */
    fun currentUserId(): String

    /**
     * Returns a member with memberId.
     *
     * @param memberId id of the member.
     *
     * @return [Task] with [IMemberModel] in case of success.
     */
    fun member(memberId: String): Task<IMemberModel, Throwable>

    /**
     * Returns members with memberIds.
     *
     * @param memberIds ids of the members.
     *
     * @return [Task] with List<[IMemberModel]> in case of success.
     */
    fun members(memberIds: List<String>): Task<List<IMemberModel>, Throwable>
}