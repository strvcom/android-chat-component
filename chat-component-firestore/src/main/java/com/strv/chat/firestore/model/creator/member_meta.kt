package com.strv.chat.firestore.model.creator

import com.strv.chat.core.domain.model.IMemberMetaModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.model.FirestoreMemberMetaModel

internal object MemberMetaModelCreator : Creator<IMemberMetaModel, MemberMetaModelConfiguration> {

    override val create: MemberMetaModelConfiguration.() -> IMemberMetaModel = {
        FirestoreMemberMetaModel(id, name)
    }
}

internal class MemberMetaModelConfiguration(
    val id: String,
    val name: String
) : CreatorConfiguration