package com.strv.chat.core.domain.model

interface IMemberModel {
    val memberId: String
    val memberName: String
    val memberPhotoUrl: String
}

interface IMemberMetaModel {
    val memberId: String
    val memberName: String
}