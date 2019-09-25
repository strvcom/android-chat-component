package com.strv.chat.library.core.ui.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.strv.chat.library.R

fun ImageView.imageCircleUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions().apply { circleCrop() })
        .placeholder(R.drawable.placeholder_avatar_round)
        .error(R.drawable.placeholder_avatar_round)
        .into(this)
}

fun ImageView.imageCenterCropUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.placeholder_image)
        .centerCrop()
        .error(R.drawable.placeholder_image)
        .into(this)
}

fun ImageView.imageUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .centerCrop()
        .transition(withCrossFade())
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.placeholder_image)
        .into(this)
}
