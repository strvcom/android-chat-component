package com.strv.chat.component.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.strv.chat.component.R
import com.strv.chat.component.ui.base.BaseActivity
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.messages.ChatRecyclerView
import com.strv.chat.core.core.ui.chat.sending.SendWidget
import com.strv.chat.core.core.ui.extensions.REQUEST_IMAGE_CAPTURE
import com.strv.chat.core.core.ui.extensions.REQUEST_IMAGE_GALLERY

const val CONVERSATION_ID_EXTRA = "conversation_id"

class ChatActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context, conversationId: String) = Intent(context, ChatActivity::class.java).apply {
            putExtra(CONVERSATION_ID_EXTRA, conversationId)
        }
    }

    val chatRecyclerView by lazy {
        findViewById<ChatRecyclerView>(R.id.rv_chat)
    }

    val sendWidget by lazy {
        findViewById<SendWidget>(R.id.w_send)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    //normally would be in ViewModel
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView.init(
            requireNotNull(intent.getStringExtra(CONVERSATION_ID_EXTRA)),
            { itemView ->
                when (itemView) {
                    is ChatItemView.Header -> {
                    }
                    is ChatItemView.MyTextMessage -> {
                    }
                    is ChatItemView.OtherTextMessage -> {
                    }
                    is ChatItemView.MyImageMessage -> openImageDetail(itemView.imageUrl)
                    is ChatItemView.OtherImageMessage -> openImageDetail(itemView.imageUrl)
                }
            }
        )

        sendWidget.init(
            requireNotNull(intent.getStringExtra(CONVERSATION_ID_EXTRA)),
            controllerCompositionRoot().mediaProvider()
        )
    }

    override fun onStart() {
        super.onStart()

        chatRecyclerView.onStart()
            .onNext {
                progress.visibility = View.GONE
            }.onError {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStop() {
        super.onStop()

        chatRecyclerView.onStop()
        sendWidget.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    sendWidget.uploadImage(
                        requireNotNull(uri) { "Uri has to be defined" }
                    )
                }
                REQUEST_IMAGE_GALLERY -> {
                    data?.data?.let { uri ->
                        sendWidget.uploadImage(uri)

                    }
                }
            }
        }
    }

    private fun openImageDetail(imageUrl: String) {
        ImageDetailActivity.newIntent(this, imageUrl)
    }
}
