package com.strv.chat.component.business

import com.strv.chat.component.model.members
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.provider.ChatMemberProvider

class ChatMemberProviderImpl : ChatMemberProvider {

    override fun member(memberId: String): IMemberModel =
        members.first { member ->
            member.memberId == memberId
        }
}