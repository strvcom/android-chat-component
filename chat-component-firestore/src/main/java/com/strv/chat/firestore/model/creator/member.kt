package com.strv.chat.firestore.model.creator

import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.domain.model.IMemberModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreMemberEntity
import com.strv.chat.firestore.entity.NAME
import com.strv.chat.firestore.entity.PHOTO_URL
import com.strv.chat.firestore.model.FirestoreMemberModel

object MemberModelCreator : Creator<IMemberModel, MemberModelConfiguration> {

    override val create: MemberModelConfiguration.() -> IMemberModel = {
        FirestoreMemberModel(
            requireNotNull(member.id) { "$ID must me specified" },
            requireNotNull(member.name) { "$NAME must me specified" },
            requireNotNull(member.photoUrl) { "$PHOTO_URL must me specified" }
        )
    }
}

class MemberModelConfiguration(
    val member: FirestoreMemberEntity
) : CreatorConfiguration