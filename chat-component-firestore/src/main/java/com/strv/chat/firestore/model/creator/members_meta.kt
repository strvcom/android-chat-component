package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMembersMetaEntity

object MemberMetaModelsCreator : Creator<List<IMemberMetaModel>, MemberMetaModelsConfiguration> {

    override val create: MemberMetaModelsConfiguration.() -> List<IMemberMetaModel> = {
        members.map { (id, name) ->
            MemberMetaModelCreator.create(MemberMetaModelConfiguration(id, name))
        }
    }
}

class MemberMetaModelsConfiguration(
    val members: FirestoreMembersMetaEntity
) : CreatorConfiguration