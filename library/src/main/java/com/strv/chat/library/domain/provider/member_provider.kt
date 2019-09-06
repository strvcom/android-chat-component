package com.strv.chat.library.domain.provider

interface MemberModel {
    val userId: String
    val userName: String
    val userPhotoUrl: String
}

interface MemberProvider {

    fun currentUserId(): String

    fun member(memberId: String): MemberModel
}