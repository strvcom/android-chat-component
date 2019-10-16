package com.strv.chat.core.domain.provider

import com.strv.chat.core.domain.model.IMemberModel

interface ChatMemberProvider {

    fun member(memberId: String): IMemberModel
}