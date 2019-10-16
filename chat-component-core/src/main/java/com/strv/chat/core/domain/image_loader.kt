package com.strv.chat.core.domain

import android.widget.ImageView

interface ImageLoader {

    fun loadAvatar(imageView: ImageView, url: String)
    fun loadImageMessage(imageView: ImageView, url: String)
    fun loadImageDetail(imageView: ImageView, url: String)
}