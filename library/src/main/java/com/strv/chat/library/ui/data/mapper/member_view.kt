package com.strv.chat.library.ui.data.mapper

import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.ui.data.MemberView

internal fun memberView(memberModel: MemberModel) =
    MemberView(memberModel.userName, memberModel.userPhotoUrl)