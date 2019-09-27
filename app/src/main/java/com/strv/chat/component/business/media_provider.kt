package com.strv.chat.component.business

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.strv.chat.library.domain.provider.MediaProvider
import java.text.SimpleDateFormat
import java.util.*

class MediaProviderImpl : MediaProvider {

    override var uri: Uri? = null

    private val fileName
        get() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    private val directoryName = "Chat_component"

    override fun newImageUri(context: Context): Uri =
        imageFile(context, fileName, directoryName).also {
            uri = it
        }

    private fun imageFile(context: Context, fileName: String, albumDirectoryName: String): Uri {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${fileName}.jpg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$albumDirectoryName")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        return requireNotNull(
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        )
    }
}
