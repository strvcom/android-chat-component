package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMemberEntity

internal object MemberModelsCreator : Creator<List<IMemberModel>, MemberModelsConfiguration> {

    override val create: MemberModelsConfiguration.() -> List<IMemberModel> = {
        members.map { entity ->
            MemberModelCreator.create(MemberModelConfiguration(entity))
        }
    }
}

internal class MemberModelsConfiguration(
    val members: List<FirestoreMemberEntity>
) : CreatorConfiguration