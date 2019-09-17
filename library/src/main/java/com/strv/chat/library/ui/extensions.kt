package com.strv.chat.library.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.strv.chat.library.R
import com.strv.chat.library.ui.view.ItemListDialogFragment

const val REQUEST_IMAGE_CAPTURE = 1000

typealias OnClickAction<T> = (T) -> Unit

fun ImageView.imageCircle(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions().apply { circleCrop() })
        .placeholder(R.drawable.placeholder_avatar_round)
        .error(R.drawable.placeholder_avatar_round)
        .into(this)
}

fun ImageView.imageUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.placeholder_image)
        .into(this)
}

fun TextView.reset() {
    text = ""
}

fun selector(title: String?, list: Array<String>, setup: ItemListDialogFragment.() -> Unit) =
    ItemListDialogFragment.newInstance(title, list).apply(setup)

fun Activity.openCamera(imageUri: Uri) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        resolveActivity(packageManager)?.let {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(this, REQUEST_IMAGE_CAPTURE)
        }
    }
}