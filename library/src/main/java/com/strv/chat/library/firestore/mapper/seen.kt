package com.strv.chat.library.firestore.mapper

import com.strv.chat.library.domain.model.MessageModelResponse
import com.strv.chat.library.firestore.entity.FirestoreSeen

internal fun seenEntity(model: MessageModelResponse) =
    FirestoreSeen(model.id)