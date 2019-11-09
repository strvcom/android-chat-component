package com.strv.chat.firestore.model

import com.strv.chat.core.domain.model.IImageMessageModel
import com.strv.chat.core.domain.model.IImageModel
import com.strv.chat.core.domain.model.ITextMessageModel
import java.util.Date

internal data class FirestoreTextMessageModel(
    override val id: String,
    override val sentDate: Date,
    override val senderId: String,
    override val text: String
) : ITextMessageModel

internal data class FirestoreImageMessageModel(
    override val id: String,
    override val sentDate: Date,
    override val senderId: String,
    override val imageModel: IImageModel
) : IImageMessageModel

internal data class FirestoreImageModel(
    override val url: String
): IImageModel