package com.strv.chat.firestore.model

import com.strv.chat.core.domain.model.IMemberModel

internal data class FirestoreMemberModel(
    override val memberId: String,
    override val memberName: String,
    override val memberPhotoUrl: String
): IMemberModel