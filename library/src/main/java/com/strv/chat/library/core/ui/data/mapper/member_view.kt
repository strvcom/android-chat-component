package com.strv.chat.library.core.ui.data.mapper

import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.core.ui.data.MemberView

internal fun memberView(memberModel: MemberModel) =
    MemberView(memberModel.userName, memberModel.userPhotoUrl)