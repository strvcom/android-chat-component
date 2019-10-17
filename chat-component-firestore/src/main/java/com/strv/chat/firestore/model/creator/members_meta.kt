package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMemberMetaEntity
import com.strv.chat.firestore.entity.NAME

object MemberMetaModelsCreator : Creator<List<IMemberMetaModel>, MemberMetaModelsConfiguration> {

    override val create: MemberMetaModelsConfiguration.() -> List<IMemberMetaModel> = {
        membersMeta.map { (id, memberMeta) ->
            MemberMetaModelCreator.create(
                MemberMetaModelConfiguration(
                    id,
                    requireNotNull(memberMeta.name) { "$NAME has to be defined" })
            )
        }
    }
}

class MemberMetaModelsConfiguration(
    val membersMeta: Map<String, FirestoreMemberMetaEntity>
) : CreatorConfiguration