package com.strv.chat.component.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strv.chat.component.R
import com.strv.chat.core.core.ui.conversation.ConversationRecyclerView

class ConversationsActivity : AppCompatActivity() {

    private val conversationRecyclerView by lazy {
        findViewById<ConversationRecyclerView>(R.id.rv_chat)
    }

    private val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        conversationRecyclerView.init {
            onConversationClick = { conversation ->
                openChat(conversation.id, conversation.otherMemberIds)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        conversationRecyclerView.onStart()
            .onError { error ->
                showErrorToast(error.localizedMessage ?: "Unknown error")
            }.onNext {
                progress.visibility = View.GONE
            }
    }

    override fun onStop() {
        super.onStop()

        conversationRecyclerView.onStop()
    }

    private fun openChat(conversationId: String, otherMemberIds: List<String>) {
        startActivity(
            ChatActivity.newIntent(
                this@ConversationsActivity,
                conversationId,
                otherMemberIds
            )
        )
    }

    private fun showErrorToast(errorMessage: String) {
        Toast.makeText(this@ConversationsActivity, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
