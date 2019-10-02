package com.strv.chat.component.business

import com.strv.chat.component.model.CURRENT_USER_ID
import com.strv.chat.component.model.members
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.provider.MemberProvider

class MemberProviderImpl : MemberProvider {
    override fun currentUserId(): String =
        CURRENT_USER_ID

    override fun member(memberId: String): IMemberModel =
        members.first { member ->
            member.userId == memberId
        }
}