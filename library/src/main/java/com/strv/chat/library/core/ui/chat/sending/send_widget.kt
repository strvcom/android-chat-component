package com.strv.chat.library.core.ui.chat.sending

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import com.strv.chat.library.R
import com.strv.chat.library.core.session.ChatComponent.chatClient
import com.strv.chat.library.core.ui.extensions.openCamera
import com.strv.chat.library.core.ui.extensions.openGalleryPhotoPicker
import com.strv.chat.library.core.ui.extensions.reset
import com.strv.chat.library.core.ui.extensions.selector
import com.strv.chat.library.core.ui.view.DIALOG_PHOTO_PICKER
import com.strv.chat.library.domain.Disposable
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelRequest.ImageMessageModel.Image
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MediaProvider
import com.strv.chat.library.domain.provider.MemberProvider
import strv.ktools.logD
import strv.ktools.logE
import strv.ktools.logMe
import java.util.*

class SendWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val disposable = LinkedList<Disposable>()

    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider
    private lateinit var mediaProvider: MediaProvider

    private val buttonSend by lazy {
        findViewById<ImageButton>(R.id.ib_send)
    }

    private val editInput by lazy {
        findViewById<EditText>(R.id.et_message_input)
    }

    private val buttonImage by lazy {
        findViewById<ImageButton>(R.id.ib_image)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_send_widget, this)
        buttonSendListener()
        buttonCameraListener()
    }

    fun init(
        conversationProvider: ConversationProvider,
        memberProvider: MemberProvider,
        mediaProvider: MediaProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(conversationProvider, memberProvider, mediaProvider).apply(
            config
        )
            .build()
    }

    fun onStop() {
        while (disposable.isNotEmpty()) {
            disposable.pop().dispose()
        }
    }

    fun uploadImage(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    memberProvider.currentUserId(),
                    conversationProvider.conversationId
                )
            )
        } else {
            context.startService(
                UploadPhotoService.newIntent(
                    context,
                    uri.toString(),
                    memberProvider.currentUserId(),
                    conversationProvider.conversationId
                )
            )
        }
    }


    private fun sendImageMessage(messageUrl: String) {
        sendMessage(
            MessageModelRequest.ImageMessageModel(
                senderId = memberProvider.currentUserId(),
                conversationId = conversationProvider.conversationId,
                image = Image(0.0, 0.0, messageUrl.toString())
            )
        )
    }

    private fun buttonSendListener() {
        buttonSend.setOnClickListener {
            if (editInput.text.isNotBlank()) {
                sendTextMessage(memberProvider.currentUserId(), editInput.text.toString())
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
        //todo update
        selector("Choose photo", arrayOf("Take photo", "Select from library")) {
            onClick { position ->
                val uri = mediaProvider.imageUri()
                uri.logMe()
                when (position) {
                    //todo how to pass application name here

                    0 -> activity?.openCamera(uri)
                    1 -> activity?.openGalleryPhotoPicker("Select photo")
                }
            }
        }.show((context as FragmentActivity).supportFragmentManager, DIALOG_PHOTO_PICKER)
    }

    private fun sendTextMessage(userId: String, message: String) {
        sendMessage(
            MessageModelRequest.TextMessageModel(
                senderId = userId,
                conversationId = conversationProvider.conversationId,
                text = message
            )
        )
    }

    private fun sendMessage(message: MessageModelRequest) {
        disposable.add(
            chatClient().sendMessage(message)
                .onError { error ->
                    logE(error.localizedMessage ?: "Unknown error")
                }.onSuccess {
                    logD("Message $it has been sent")
                }
        )
    }

    inner class Builder(
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider,
        val mediaProvider: MediaProvider
    ) {

        fun build() {
            this@SendWidget.memberProvider = memberProvider
            this@SendWidget.conversationProvider = conversationProvider
            this@SendWidget.mediaProvider = mediaProvider
        }
    }
}