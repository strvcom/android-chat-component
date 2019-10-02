package com.strv.chat.component.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.strv.chat.component.R
import com.strv.chat.component.ui.base.BaseActivity
import com.strv.chat.core.core.ui.conversation.ConversationRecyclerView

class ConversationsActivity : BaseActivity() {

    val conversationRecyclerView by lazy {
        findViewById<ConversationRecyclerView>(R.id.rv_chat)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        conversationRecyclerView.init { conversation ->
            openChat(conversation.id)
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

    private fun openChat(conversationId: String) =
        startActivity(ChatActivity.newIntent(this@ConversationsActivity, conversationId))

    private fun showErrorToast(errorMessage: String) {
        Toast.makeText(this@ConversationsActivity, errorMessage, Toast.LENGTH_SHORT).show()
    }
}


