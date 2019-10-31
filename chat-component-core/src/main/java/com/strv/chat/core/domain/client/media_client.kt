package com.strv.chat.core.domain.client

import android.graphics.Bitmap
import android.net.Uri
import com.strv.chat.core.domain.task.ProgressTask
import com.strv.chat.core.domain.task.Task

typealias DownloadUrl = Uri
typealias UploadUrl = String

/**
 * Provides interaction with a storage service.
 */
interface MediaClient {

    /**
     * Obtains an url for a new photo upload.
     *
     * @param fileName name of the file.
     *
     * @return [Task] with uploadUrl in case of success.
     */
    fun uploadUrl(fileName: String): Task<UploadUrl, Throwable>

    /**
     * Upload an image to a specified url.
     *
     * @param bitmap image to upload.
     * @param uploadUrl url where the image should be uploaded.
     * @param contentType content type of the image.
     *
     * @return [Task] with downloadUrl in case of success.
     */
    fun uploadImage(bitmap: Bitmap, uploadUrl: String, contentType: String = "imageModel/jpeg"): ProgressTask<DownloadUrl, Throwable>
}

