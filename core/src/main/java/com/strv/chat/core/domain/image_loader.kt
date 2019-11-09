package com.strv.chat.core.domain

import android.widget.ImageView

const val IMAGE_LOADER = "imageLoader"

/**
 * Provides methods for loading image's url into [ImageView]
 */
interface ImageLoader {

    /**
     * Loads a user profile picture
     *
     * @param imageView view where to load resource
     * @param url image url
     */
    fun loadAvatar(imageView: ImageView, url: String?)

    /**
     * Loads a picture for an image message
     *
     * @param imageView view where to load resource
     * @param url image url
     */
    fun loadImageMessage(imageView: ImageView, url: String?)

    /**
     * Loads a picture for an image detail
     *
     * @param imageView view where to load a resource
     * @param url image url
     */
    fun loadImageDetail(imageView: ImageView, url: String?)
}