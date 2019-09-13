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
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.reset
import strv.ktools.logE
import strv.ktools.logI

class SendWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var chatClient: ChatClient
    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider

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

    operator fun invoke(
        chatClient: ChatClient,
        conversationProvider: ConversationProvider,
        memberProvider: MemberProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(chatClient, conversationProvider, memberProvider).apply(config).build()
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
                conversationId = conversationProvider.conversationId,
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
        val chatClient: ChatClient,
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider
    ) {

        fun build() {
            this@SendWidget.chatClient = chatClient
            this@SendWidget.memberProvider = memberProvider
            this@SendWidget.conversationProvider = conversationProvider
        }
    }
}