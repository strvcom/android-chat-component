package com.strv.chat.core.core.ui.chat.sending

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import com.strv.chat.core.core.ui.extensions.autoCancel
import com.strv.chat.core.core.ui.extensions.contentTitle
import com.strv.chat.core.core.ui.extensions.dismissible
import com.strv.chat.core.core.ui.extensions.largeIcon
import com.strv.chat.core.core.ui.extensions.notification
import com.strv.chat.core.core.ui.extensions.progress
import com.strv.chat.core.core.ui.extensions.smallIcon
import com.strv.chat.core.core.ui.extensions.toBitmap
import com.strv.chat.core.domain.task.Disposable
import com.strv.chat.core.domain.task.Task
import com.strv.chat.core.domain.client.DownloadUrl
import com.strv.chat.core.domain.collect
import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.core.domain.model.MessageInputModel.ImageInputModel.ImageModel
import com.strv.chat.core.domain.task.flatMap
import strv.ktools.logD
import strv.ktools.logE
import java.util.LinkedList

private const val ARGUMENT_PHOTO_URI = "uri"
private const val ARGUMENT_SENDER_ID = "sender_id"
private const val ARGUMENT_CONVERSATION_ID = "conversation_id"
private const val ARGUMENT_SERVICE_ID = "service_id"

class UploadPhotoService : IntentService("UploadPhotoService") {

    companion object {
        fun newIntent(context: Context, fileUri: String, senderId: String, conversationId: String) =
            Intent(context, UploadPhotoService::class.java).apply {
                putExtra(ARGUMENT_PHOTO_URI, fileUri)
                putExtra(ARGUMENT_SENDER_ID, senderId)
                putExtra(ARGUMENT_CONVERSATION_ID, conversationId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
    }

    private val disposable = LinkedList<Disposable>()

    private lateinit var uri: Uri

    private lateinit var conversationId: String
    private lateinit var senderId: String

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val id = intent.getIntExtra(ARGUMENT_SERVICE_ID, -1)

            uri = requireNotNull(intent.getStringExtra(ARGUMENT_PHOTO_URI)).toUri()
            senderId = requireNotNull(intent.getStringExtra(ARGUMENT_SENDER_ID))
            conversationId = requireNotNull(intent.getStringExtra(ARGUMENT_CONVERSATION_ID))

            startForeground(id, uploadingNotification())

            uploadImage(id, uri.lastPathSegment!!, uri.toBitmap(this))
        }
    }

    override fun onCreate() {
        super.onCreate()
        logD("UploadPhotoService is created")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.putExtra(ARGUMENT_SERVICE_ID, startId)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        logD("UploadPhotoService is destroyed")

        disposable.collect(Disposable::dispose)

        super.onDestroy()
    }

    private fun uploadImage(startId: Int, name: String, bitmap: Bitmap) =
        chatComponent.mediaClient().uploadUrl(name)
            .flatMap { url ->
                chatComponent.mediaClient().uploadImage(bitmap, url)
                    .onProgress { progress ->
                        showNotification(startId, uploadingNotification(progress))
                    }
            }.flatMap { url ->
                sendImageMessage(url)
            }.onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
                showNotification(startId, errorNotification())
            }.onSuccess { id ->
                logD("IImageModel message $id has been sent")
                showNotification(startId, doneNotification())
            }

    private fun sendImageMessage(messageUrl: DownloadUrl): Task<String, Throwable> =
        sendMessage(
            MessageInputModel.ImageInputModel(
                senderId = senderId,
                conversationId = conversationId,
                imageModel = ImageModel(0.0, 0.0, messageUrl.toString())
            )
        )

    private fun sendMessage(message: MessageInputModel) =
        chatComponent.chatClient().sendMessage(message)


    private fun showNotification(notificationId: Int, notification: Notification) {
        NotificationManagerCompat
            .from(this)
            .notify(notificationId, notification)
    }

    private fun uploadingNotification(progress: Int = 0) =
        notification(chatComponent.channelId()) {
            largeIcon = requireNotNull(
                ContextCompat.getDrawable(
                    this@UploadPhotoService,
                    chatComponent.largeIconRes()
                )
            ).toBitmap()
            dismissible = false
            smallIcon = chatComponent.smallIconProgressRes()
            contentTitle = chatComponent.string(R.string.uploading_photo)
            autoCancel = true
            this.progress {
                this.max = 100
                this.progress = progress
            }
        }

    private fun errorNotification() =
        notification(chatComponent.channelId()) {
            largeIcon = requireNotNull(
                ContextCompat.getDrawable(
                    this@UploadPhotoService,
                    chatComponent.largeIconRes()
                )
            ).toBitmap()
            smallIcon = chatComponent.smallIconErrorRes()
            contentTitle = chatComponent.string(R.string.photo_was_not_uploaded)
            autoCancel = true
        }

    private fun doneNotification() =
        notification(chatComponent.channelId()) {
            largeIcon = requireNotNull(
                ContextCompat.getDrawable(
                    this@UploadPhotoService,
                    chatComponent.largeIconRes()
                )
            ).toBitmap()
            smallIcon = chatComponent.smallIconSuccessRes()
            contentTitle = chatComponent.string(R.string.photo_was_uploaded)
            autoCancel = true
        }
}