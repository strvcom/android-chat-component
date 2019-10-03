package com.strv.chat.core.core.ui.data.creator

import com.strv.chat.core.core.ui.data.MemberView
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration

object MemberViewCreator : Creator<MemberView, MemberViewConfiguration> {
    override val create: MemberViewConfiguration.() -> MemberView = {
        MemberView(member.userName, member.userPhotoUrl)
    }
}

class MemberViewConfiguration(
    val member: IMemberModel
) : CreatorConfiguration