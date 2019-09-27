package com.strv.chat.library.core.ui.data.mapper

import com.strv.chat.library.core.ui.data.MemberView
import com.strv.chat.library.domain.model.IMemberModel

internal fun memberView(memberModel: IMemberModel) =
    MemberView(memberModel.userName, memberModel.userPhotoUrl)