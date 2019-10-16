package com.strv.chat.core.core.ui.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

const val REQUEST_IMAGE_CAPTURE = 1000
const val REQUEST_IMAGE_GALLERY = 1001

internal fun Activity.openCamera(imageUri: Uri) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        resolveActivity(packageManager)?.let {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(this, REQUEST_IMAGE_CAPTURE)
        }
    }
}

internal fun Activity.openGalleryPhotoPicker(title: String) =
    startActivityForResult(
        Intent.createChooser(
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }, title), REQUEST_IMAGE_GALLERY
    )