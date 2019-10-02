package com.strv.chat.firestore.model.mapper

import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.firestore.entity.FirestoreSeen

internal fun seenEntity(model: IMessageModel) =
    FirestoreSeen(model.id)