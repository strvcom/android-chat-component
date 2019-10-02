package com.strv.chat.core.core.ui.data.mapper

import com.strv.chat.core.core.ui.data.MemberView
import com.strv.chat.core.domain.model.IMemberModel

internal fun memberView(memberModel: IMemberModel) =
    MemberView(memberModel.userName, memberModel.userPhotoUrl)