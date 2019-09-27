package com.strv.chat.library.firestore.model.mapper

import com.strv.chat.library.domain.model.IMessageModel
import com.strv.chat.library.firestore.entity.FirestoreSeen

internal fun seenEntity(model: IMessageModel) =
    FirestoreSeen(model.id)