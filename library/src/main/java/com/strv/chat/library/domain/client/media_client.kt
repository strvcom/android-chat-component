package com.strv.chat.library.domain.client

import android.graphics.Bitmap
import android.net.Uri
import com.strv.chat.library.domain.ProgressTask
import com.strv.chat.library.domain.Task

typealias DownloadUrl = Uri
typealias UploadUrl = String

interface MediaClient {

    fun uploadUrl(fileName: String): Task<UploadUrl, Throwable>

    fun uploadImage(bitmap: Bitmap, uploadUrl: String, contentType: String = "image/jpeg"): ProgressTask<DownloadUrl, Throwable>
}

