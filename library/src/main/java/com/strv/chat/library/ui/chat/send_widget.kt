package com.strv.chat.library.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import com.strv.chat.library.R
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.reset
import strv.ktools.logE
import strv.ktools.logI
import java.util.*

class SendWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var chatClient: ChatClient
    private lateinit var memberProvider: MemberProvider

    private val buttonSend by lazy {
        findViewById<ImageButton>(R.id.ib_send)
    }

    private val editInput by lazy {
        findViewById<EditText>(R.id.et_message_input)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_send_widget, this)
        buttonSendListener()
    }

    operator fun invoke(config: Builder.() -> Unit) {
        Builder().apply(config).build()
    }

    private fun buttonSendListener() {
        buttonSend.setOnClickListener {
            if (editInput.text.isNotBlank()) {
                sendTextMessage(memberProvider.currentUserId(), editInput.text.toString())
                editInput.reset()
            }
        }
    }

    private fun sendTextMessage(userId: String, message: String) {
        chatClient.sendMessage(
            MessageModelRequest.TextMessageModel(
                senderId = userId,
                text = message
            ),
            object : Observer<Void?> {
                override fun onSuccess(response: Void?) {
                    logI("Message has been sent")
                }

                override fun onError(error: Throwable) {
                    logE(error.localizedMessage)
                }
            }
        )
    }

    inner class Builder(
        var chatClient: ChatClient? = null,
        var memberProvider: MemberProvider? = null
    ) {

        fun build() {
            this@SendWidget.chatClient =
                requireNotNull(chatClient) { "ChatClient must be specified" }
            this@SendWidget.memberProvider =
                requireNotNull(memberProvider) { "MemberProvider must be specified" }
        }
    }
}