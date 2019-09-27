package com.strv.chat.library.core.ui.data.mapper

import com.strv.chat.library.core.ui.data.MemberView
import com.strv.chat.library.domain.model.MemberModel

internal fun memberView(memberModel: MemberModel) =
    MemberView(memberModel.userName, memberModel.userPhotoUrl)