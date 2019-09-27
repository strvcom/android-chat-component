package com.strv.chat.library.domain.model

import java.util.Date

interface IMessageModel {
    val id: String
    val sentDate: Date
    val senderId: String
}

interface ITextMessageModel : IMessageModel {
    val text: String
}

interface IImageMessageModel : IMessageModel {
    val imageModel: IImageModel
}

interface IImageModel {
    val width: Double
    val height: Double
    val original: String
}