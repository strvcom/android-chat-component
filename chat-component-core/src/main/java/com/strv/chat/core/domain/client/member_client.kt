package com.strv.chat.core.domain.client

import com.strv.chat.core.domain.Task
import com.strv.chat.core.domain.model.IMemberModel

interface MemberClient {

    fun currentUserId(): String

    fun member(memberId: String): Task<IMemberModel, Throwable>

    fun members(memberIds: List<String>): Task<List<IMemberModel>, Throwable>
}