package com.strv.chat.core.core.ui.chat.sending

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.strv.chat.core.R
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent
import com.strv.chat.core.core.ui.chat.sending.style.SendWidgetStyle
import com.strv.chat.core.core.ui.extensions.openCamera
import com.strv.chat.core.core.ui.extensions.openGalleryPhotoPicker
import com.strv.chat.core.core.ui.extensions.reset
import com.strv.chat.core.core.ui.extensions.selector
import com.strv.chat.core.core.ui.extensions.tint
import com.strv.chat.core.core.ui.view.DIALOG_PHOTO_PICKER
import com.strv.chat.core.domain.model.MessageInputModel
import com.strv.chat.core.domain.provider.FileProvider
import strv.ktools.logD
import strv.ktools.logE

class SendWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var conversationId: String
    private lateinit var fileProvider: FileProvider

    private val buttonSend by lazy {
        findViewById<ImageButton>(R.id.ib_send)
    }

    private val editInput by lazy {
        findViewById<EditText>(R.id.et_message_input)
    }

    private val buttonText by lazy {
        findViewById<ImageButton>(R.id.ib_text)
    }

    private val buttonImage by lazy {
        findViewById<ImageButton>(R.id.ib_image)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_send_widget, this)

        if (attrs != null) {
            applyStyle(SendWidgetStyle.parse(context, attrs))
        }

        buttonSendListener()
        buttonCameraListener()
    }

    fun init(
        conversationId: String,
        fileProvider: FileProvider
    ) {
        this@SendWidget.conversationId = conversationId
        this@SendWidget.fileProvider = fileProvider
    }

    fun uploadImage(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    chatComponent.currentUserId,
                    conversationId
                )
            )
        } else {
            context.startService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    chatComponent.currentUserId,
                    conversationId
                )
            )
        }
    }

    private fun buttonSendListener() {
        buttonSend.setOnClickListener {
            if (editInput.text.isNotBlank()) {
                sendTextMessage(chatComponent.currentUserId, editInput.text.toString())
                editInput.reset()
            }
        }
    }

    private fun buttonCameraListener() {
        buttonImage.setOnClickListener {
            showPhotoPickerDialog()
        }
    }

    private fun showPhotoPickerDialog() {
        selector(
            chatComponent.string(R.string.choose_photo),
            arrayOf(
                chatComponent.string(R.string.take_photo),
                chatComponent.string(R.string.select_from_library)
            )
        ) {
            onClick { position ->
                val uri = fileProvider.newFile(requireContext())
                when (position) {
                    0 -> activity?.openCamera(uri)
                    1 -> activity?.openGalleryPhotoPicker(chatComponent.string(R.string.select_photo))
                }
            }
        }.show((context as FragmentActivity).supportFragmentManager, DIALOG_PHOTO_PICKER)
    }

    private fun sendTextMessage(userId: String, message: String) {
        sendMessage(
            MessageInputModel.TextInputModel(
                senderId = userId,
                conversationId = conversationId,
                text = message
            )
        )
    }

    private fun sendMessage(message: MessageInputModel) {
        chatComponent.chatClient().sendMessage(message)
            .onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
            }.onSuccess {
                logD("Message $it has been sent")
            }
    }

    private fun applyStyle(style: SendWidgetStyle) {
        setBackgroundColor(style.backgroundColor)
        buttonSend.tint(ColorStateList.valueOf(style.sendIconTint))
        buttonText.setColorFilter(style.filterColorActivated)
        buttonImage.setColorFilter(style.filterColorNormal)
    }
}