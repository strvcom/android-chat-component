package com.strv.chat.library.cloudStorage.client

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.library.cloudStorage.IMAGE_REFERENCE
import com.strv.chat.library.cloudStorage.contentType
import com.strv.chat.library.cloudStorage.path
import com.strv.chat.library.cloudStorage.storageMetadata
import com.strv.chat.library.domain.Task
import com.strv.chat.library.domain.client.DownloadUrl
import com.strv.chat.library.domain.client.MediaClient
import com.strv.chat.library.domain.client.UploadUrl
import com.strv.chat.library.domain.progressTask
import com.strv.chat.library.domain.task
import strv.ktools.logD
import java.io.ByteArrayOutputStream

class CloudStorageMediaClient(
    val firebaseStorage: FirebaseStorage
) : MediaClient {

    override fun uploadUrl(fileName: String): Task<UploadUrl, Throwable> = task {
        invokeSuccess("$IMAGE_REFERENCE/$fileName")
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
                //todo should I add context????
                .addOnSuccessListener() {
                    logD("File was uploaded")
                }
                .addOnFailureListener {
                    invokeError(it)
                }
                .addOnProgressListener { snapshot ->
                    invokeProgress((100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt())
                }
                .continueWithTask {
                    imagePath.downloadUrl
                }.addOnSuccessListener { uri ->
                    invokeSuccess(uri)
                }.addOnFailureListener {
                    invokeError(it)
                }
        }
}