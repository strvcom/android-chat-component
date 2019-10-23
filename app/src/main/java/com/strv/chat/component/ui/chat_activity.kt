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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.strv.chat.component.R
import com.strv.chat.component.toLiveData
import com.strv.chat.core.core.ui.chat.data.ChatItemView
import com.strv.chat.core.core.ui.chat.messages.ChatRecyclerView
import com.strv.chat.core.core.ui.chat.sending.SendWidget
import com.strv.chat.core.core.ui.extensions.REQUEST_IMAGE_CAPTURE
import com.strv.chat.core.core.ui.extensions.REQUEST_IMAGE_GALLERY
import com.strv.chat.core.domain.ImageLoader
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.core.domain.provider.FileProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val CONVERSATION_ID_EXTRA = "conversation_id"
const val OTHER_MEMBER_IDS_EXTRA = "member_ids"

class ChatActivity : AppCompatActivity(), FileProvider {
    companion object {
        fun newIntent(context: Context, conversationId: String, otherMemberIds: List<String>) =
            Intent(context, ChatActivity::class.java).apply {
                putExtra(CONVERSATION_ID_EXTRA, conversationId)
                putExtra(OTHER_MEMBER_IDS_EXTRA, otherMemberIds.toTypedArray())
            }
    }

    private val chatViewModel: ChatViewModel by viewModel {
        parametersOf(
            requireNotNull(intent.getStringExtra(CONVERSATION_ID_EXTRA)) { "$CONVERSATION_ID_EXTRA must be defined" },
            requireNotNull(intent.getStringArrayExtra(OTHER_MEMBER_IDS_EXTRA)) { "$OTHER_MEMBER_IDS_EXTRA must be defined" }.toList()
        )
    }

    private val loader by inject<ImageLoader>()

    private val chatRecyclerView by lazy {
        findViewById<ChatRecyclerView>(R.id.rv_chat)
    }

    private val sendWidget by lazy {
        findViewById<SendWidget>(R.id.w_send)
    }

    private val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView.init {
            imageLoader = loader
            onMessageClick = { itemView ->
                when (itemView) {
                    is ChatItemView.MyImageMessage -> openImageDetail(itemView.imageUrl)
                    is ChatItemView.OtherImageMessage -> openImageDetail(itemView.imageUrl)
                }
            }
        }

        sendWidget.init {
            conversationId = chatViewModel.conversationId
            newFileProvider = this@ChatActivity
        }
    }

    override fun onStart() {
        super.onStart()

        chatViewModel.members.observe(this, Observer { otherMembers ->
            chatRecyclerView.onStart(chatViewModel.conversationId, otherMembers)
                .onNext {
                    progress.visibility = View.GONE
                }.onError {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        })
    }

    override fun onStop() {
        super.onStop()

        chatRecyclerView.onStop()
        sendWidget.newFileProvider = null
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    sendWidget.uploadImage(
                        requireNotNull(chatViewModel.photoUri) { "Uri has to be defined" }
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

    override fun newFile(context: Context): Uri =
        chatViewModel.fileProvider.newFile(context).also { uri ->
            chatViewModel.photoUri = uri
        }

    private fun openImageDetail(imageUrl: String) {
        startActivity(ImageDetailActivity.newIntent(this, imageUrl))
    }
}

class ChatViewModel(
    val conversationId: String,
    val fileProvider: FileProvider,
    otherUserIds: List<String>,
    memberClient: MemberClient
) : ViewModel() {

    var photoUri: Uri? = null

    val members = members(memberClient, otherUserIds).toLiveData()

    private fun members(memberClient: MemberClient, userIds: List<String>) =
        memberClient.members(userIds)
}
