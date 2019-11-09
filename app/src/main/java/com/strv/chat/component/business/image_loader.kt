package com.strv.chat.component.business

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.strv.chat.component.R
import com.strv.chat.core.domain.ImageLoader

class ImageLoaderImpl : ImageLoader {
    override fun loadAvatar(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions().apply { circleCrop() })
            .placeholder(R.drawable.placeholder_avatar_round)
            .error(R.drawable.placeholder_avatar_round)
            .into(imageView)
    }

    override fun loadImageMessage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.placeholder_image)
            .centerCrop()
            .error(R.drawable.placeholder_image)
            .into(imageView)
    }

    override fun loadImageDetail(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .transition(withCrossFade())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(imageView)
    }
}