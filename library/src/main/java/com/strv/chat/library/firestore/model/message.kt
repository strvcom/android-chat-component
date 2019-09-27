package com.strv.chat.library.firestore.model

import com.strv.chat.library.domain.model.IImageMessageModel
import com.strv.chat.library.domain.model.IImageModel
import com.strv.chat.library.domain.model.ITextMessageModel
import java.util.*

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
    override val width: Double,
    override val height: Double,
    override val original: String
): IImageModel