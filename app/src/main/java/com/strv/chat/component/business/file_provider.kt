package com.strv.chat.component.business

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.strv.chat.core.domain.provider.FileProvider
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class FileProviderImpl(
    val directoryName: String
) : FileProvider {

    private val fileName
        get() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    override fun newFile(context: Context): Uri =
        imageFile(context, fileName, directoryName)

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
