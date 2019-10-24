package com.strv.chat.core.domain.client

import android.graphics.Bitmap
import android.net.Uri
import com.strv.chat.core.domain.task.ProgressTask
import com.strv.chat.core.domain.task.Task

typealias DownloadUrl = Uri
typealias UploadUrl = String

interface MediaClient {

    fun uploadUrl(fileName: String): Task<UploadUrl, Throwable>

    fun uploadImage(bitmap: Bitmap, uploadUrl: String, contentType: String = "imageModel/jpeg"): ProgressTask<DownloadUrl, Throwable>
}

