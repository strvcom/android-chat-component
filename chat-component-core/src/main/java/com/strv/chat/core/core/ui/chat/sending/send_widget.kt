package com.strv.chat.core.core.ui.chat.sending

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
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

class SendWidget : ConstraintLayout {

    var conversationId: String?
        get() = throw UnsupportedOperationException("")
        set(value) {
            _conversationId = value
        }

    var newFileProvider: FileProvider?
        get() = throw UnsupportedOperationException("")
        set(value) {
            _newFileProvider = value
        }

    private var _conversationId: String? = null
    private var _newFileProvider: FileProvider? = null

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

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    fun init(builder: SendWidget.() -> Unit) {
        builder()
    }

    fun uploadImage(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    chatComponent.currentUserId,
                    _conversationId!!
                )
            )
        } else {
            context.startService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    chatComponent.currentUserId,
                    _conversationId!!
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
                when (position) {
                    0 -> activity?.openCamera(_newFileProvider!!.newFile(requireContext()))
                    1 -> activity?.openGalleryPhotoPicker(chatComponent.string(R.string.select_photo))
                }
            }
        }.show((context as FragmentActivity).supportFragmentManager, DIALOG_PHOTO_PICKER)
    }

    private fun sendTextMessage(userId: String, message: String) {
        sendMessage(
            MessageInputModel.TextInputModel(
                senderId = userId,
                conversationId = _conversationId!!,
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

    private fun validateForNull() {
        fun throwError(fieldName: String) {
            throw IllegalArgumentException("$fieldName must be defined")
        }

        if (_conversationId == null) throwError(::_conversationId.name)
        if (_newFileProvider == null) throwError(::_newFileProvider.name)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        LayoutInflater.from(context).inflate(R.layout.layout_send_widget, this)

        if (attrs != null) {
            applyStyle(SendWidgetStyle.parse(context, attrs))
        }

        buttonSendListener()
        buttonCameraListener()

        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (visibility == View.VISIBLE) {
                        validateForNull()
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })
    }
}