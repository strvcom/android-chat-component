package com.strv.chat.core.core.ui.extensions

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Gets Bitmap from Uri.
 *
 * @receiver [Uri].
 */
internal fun Uri.toBitmap(context: Context) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, this))
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)

    }