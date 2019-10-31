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

/**
 * Customizable component for entering and sending text and image messages.
 */
class SendWidget : ConstraintLayout {

    /**
     * Setter of the id of the related conversation.
     */
    var conversationId: String?
        get() = throw UnsupportedOperationException("")
        set(value) {
            _conversationId = value
        }

    /**
     * [FileProvider] setter.
     */
    var newFileProvider: FileProvider?
        get() = throw UnsupportedOperationException("")
        set(value) {
            _newFileProvider = value
        }

    /**
     * Id of the related conversation.
     */
    private var _conversationId: String? = null

    /**
     * Defines a way of retrieving file uri for saving a camera output that is used to send as an image message.
     */
    private var _newFileProvider: FileProvider? = null

    //views
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

    //constructors
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

    /**
     * Type-safe builder that allows creating Kotlin-based domain-specific languages (DSLs) suitable for configuring [SendWidget] in a semi-declarative way.
     */
    fun init(builder: SendWidget.() -> Unit) {
        builder()
    }

    /**
     * Start a service that uploads the image on the server and notifies the user about the progress and the result of the upload.
     *
     * @param uri Uri of the image.
     */
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

    /**
     * Sets onClickListener for [buttonSend] so that a message is sent after click.
     */
    private fun buttonSendListener() {
        buttonSend.setOnClickListener {
            if (editInput.text.isNotBlank()) {
                sendTextMessage(chatComponent.currentUserId, editInput.text.toString())
                editInput.reset()
            }
        }
    }

    /**
     * Sets onClickListener for [buttonImage] so that a image picker dialog is shown after click.
     */
    private fun buttonCameraListener() {
        buttonImage.setOnClickListener {
            showPhotoPickerDialog()
        }
    }

    /**
     * Shows a image picker dialog, where a user can select either form "Take photo" by camera or "Select from library".
     */
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

    /**
     * Sends a text message.
     */
    private fun sendTextMessage(userId: String, message: String) {
        sendMessage(
            MessageInputModel.TextInputModel(
                senderId = userId,
                conversationId = _conversationId!!,
                text = message
            )
        )
    }

    /**
     * Sends a message.
     */
    private fun sendMessage(message: MessageInputModel) {
        chatComponent.chatClient().sendMessage(message)
            .onError { error ->
                logE(error.localizedMessage ?: "Unknown error")
            }.onSuccess {
                logD("Message $it has been sent")
            }
    }

    /**
     * Applies style.
     */
    private fun applyStyle(style: SendWidgetStyle) {
        setBackgroundColor(style.backgroundColor)
        buttonSend.tint(ColorStateList.valueOf(style.sendIconTint))
        buttonText.setColorFilter(style.filterColorActivated)
        buttonImage.setColorFilter(style.filterColorNormal)
    }


    /**
     * Checks if all necessary properties are set before starting using the component.
     *
     * @throws IllegalArgumentException if a property is not set.
     */
    private fun validateForNull() {
        fun throwError(fieldName: String) {
            throw IllegalArgumentException("$fieldName must be defined")
        }

        if (_conversationId == null) throwError(::_conversationId.name)
        if (_newFileProvider == null) throwError(::_newFileProvider.name)
    }

    /**
     * Initialization of the component called from its constructor.
     */
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