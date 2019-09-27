package com.strv.chat.library.domain.provider

import com.strv.chat.library.domain.model.IMemberModel

interface MemberProvider {

    fun currentUserId(): String

    fun member(memberId: String): IMemberModel
}