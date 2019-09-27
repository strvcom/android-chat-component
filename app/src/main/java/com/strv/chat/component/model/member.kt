package com.strv.chat.component.model

import com.strv.chat.library.domain.model.IMemberModel

const val CURRENT_USER_ID = "user-1"
private const val CURRENT_USER_NAME = "John"
private const val CURRENT_USER_IMAGE = "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"

private const val OTHER_USER_ID = "user-2"
private const val OTHER_USER_NAME = "Camila"
private const val OTHER_USER_IMAGE = "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"

val user1 = Member(CURRENT_USER_ID, CURRENT_USER_NAME, CURRENT_USER_IMAGE)
val user2 = Member(OTHER_USER_ID, OTHER_USER_NAME, OTHER_USER_IMAGE)

val members = listOf(user1, user2)

data class Member(
    override val userId: String,
    override val userName: String,
    override val userPhotoUrl: String
): IMemberModel