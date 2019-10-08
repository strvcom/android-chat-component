package com.strv.chat.core.core.ui.data.creator

import com.strv.chat.core.core.ui.data.MemberView
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration

object MemberViewsCreator : Creator<List<MemberView>, MemberViewsConfiguration> {

    override val create: MemberViewsConfiguration.() -> List<MemberView> = {
        members.map { model ->
            MemberViewCreator.create(MemberViewConfiguration(model))
        }
    }
}

class MemberViewsConfiguration(
    val members: List<IMemberModel>
) : CreatorConfiguration