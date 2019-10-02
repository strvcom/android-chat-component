package com.strv.chat.core.domain.provider

import com.strv.chat.core.domain.model.IMemberModel

interface MemberProvider {

    fun currentUserId(): String

    fun member(memberId: String): IMemberModel
}