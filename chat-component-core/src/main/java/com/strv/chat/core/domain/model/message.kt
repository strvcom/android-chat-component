package com.strv.chat.core.domain.model

import java.util.Date

/**
 * Represents a structure of a message.
 */
interface IMessageModel {

    /**
     * Id of the message.
     */
    val id: String

    /**
     * Time when the message was sent.
     */
    val sentDate: Date

    /**
     * Id of the sender.
     */
    val senderId: String
}

/**
 * Represents a structure of a text message.
 */
interface ITextMessageModel : IMessageModel {

    /**
     * Text of the message.
     */
    val text: String
}

/**
 * Represents a structure of an image message.
 */
interface IImageMessageModel : IMessageModel {

    /**
     * Image data.
     */
    val imageModel: IImageModel
}

/**
 * Represents a structure of image data.
 */
interface IImageModel {

    /**
     * Width of the image in pixels.
     */
    val width: Double

    /**
     * Height of the image in pixels.
     */
    val height: Double

    /**
     * Url of the image.
     */
    val original: String
}