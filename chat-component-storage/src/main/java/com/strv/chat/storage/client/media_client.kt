package com.strv.chat.storage.client

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.storage.IMAGE_REFERENCE
import com.strv.chat.storage.path
import com.strv.chat.storage.storageMetadata
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.client.DownloadUrl
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.UploadUrl
import com.strv.chat.core.domain.task.progressTask
import com.strv.chat.core.domain.task.task
import strv.ktools.logD
import java.io.ByteArrayOutputStream

class CloudStorageMediaClient(
    val firebaseStorage: FirebaseStorage
) : MediaClient {

    override fun uploadUrl(fileName: String): Task<UploadUrl, Throwable> =
        task {
            invokeSuccess("$IMAGE_REFERENCE/$fileName.jpg")
        }

    override fun uploadImage(bitmap: Bitmap, uploadUrl: String, contentType: String) =
        progressTask<DownloadUrl, Throwable> {
            val metadata = storageMetadata {
                this.contentType = contentType
            }

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)

            val imagePath = path(firebaseStorage, uploadUrl)

            imagePath.putBytes(bos.toByteArray(), metadata)
                .addOnSuccessListener() {
                    logD("File was uploaded")
                }.addOnFailureListener {
                    invokeError(it)
                }.addOnProgressListener { snapshot ->
                    invokeProgress((100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt())
                }.continueWithTask {
                    imagePath.downloadUrl
                }.addOnSuccessListener { uri ->
                    invokeSuccess(uri)
                }.addOnFailureListener {
                    invokeError(it)
                }
        }
}