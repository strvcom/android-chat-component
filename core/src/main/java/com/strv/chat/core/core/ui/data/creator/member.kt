package com.strv.chat.core.core.ui.data.creator

import com.strv.chat.core.core.ui.data.MemberView
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration

internal object MemberViewCreator : Creator<MemberView, MemberViewConfiguration> {
    override val create: MemberViewConfiguration.() -> MemberView = {
        MemberView(member.memberName, member.memberPhotoUrl)
    }
}

internal class MemberViewConfiguration(
    val member: IMemberModel
) : CreatorConfiguration