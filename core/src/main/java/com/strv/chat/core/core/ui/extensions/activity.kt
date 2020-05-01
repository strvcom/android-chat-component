package com.strv.chat.core.core.ui.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

/**
 * This code will be returned in onActivityResult() after capturing an image through a camera app.
 */
const val REQUEST_IMAGE_CAPTURE = 1000

/**
 * This code will be returned in onActivityResult() after picking an image through a picker.
 */
const val REQUEST_IMAGE_GALLERY = 1001

/**
 * Opens a default camera app and saves the taken image to a provided Uri
 *
 * @param imageUri - uri where the take image will be stored
 *
 * @receiver [Activity].
 */
fun Activity.openCamera(imageUri: Uri) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        resolveActivity(packageManager)?.let {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(this, REQUEST_IMAGE_CAPTURE)
        }
    }
}

/**
 * Opens a default image picker.
 *
 * @param title of the gallery picker
 *
 * @receiver [Activity]
 */
fun Activity.openGalleryPhotoPicker(title: String) =
    startActivityForResult(
        Intent.createChooser(
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }, title), REQUEST_IMAGE_GALLERY
    )