package com.strv.chat.firestore.entity.creator

import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMemberMetaEntity

internal object MemberMetaEntityCreator : Creator<FirestoreMemberMetaEntity, MemberMetaEntityConfiguration> {

    override val create: MemberMetaEntityConfiguration.() -> FirestoreMemberMetaEntity = {
        FirestoreMemberMetaEntity(name)
    }
}

internal class MemberMetaEntityConfiguration(
    val name: String
) : CreatorConfiguration