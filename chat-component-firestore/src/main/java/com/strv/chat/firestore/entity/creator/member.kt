package com.strv.chat.firestore.entity.creator

import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMemberEntity

internal object MemberEntityCreator :
    Creator<FirestoreMemberEntity, MemberEntityConfiguration> {

    override val create: MemberEntityConfiguration.() -> FirestoreMemberEntity = {
        FirestoreMemberEntity(
            memberModel.memberId,
            memberModel.memberName,
            memberModel.memberPhotoUrl
        )
    }
}

internal class MemberEntityConfiguration(
    val memberModel: IMemberModel
) : CreatorConfiguration