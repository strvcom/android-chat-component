package com.strv.chat.component

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strv.chat.library.core.ui.conversation.ConversationRecyclerView
import com.strv.chat.library.domain.model.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider

class ConversationsActivity : AppCompatActivity() {

    val conversationRecyclerView by lazy {
        findViewById<ConversationRecyclerView>(R.id.rv_chat)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        conversationRecyclerView.init( { startActivity(MainActivity.newIntent(this@ConversationsActivity)) }
        )
    }


    override fun onStart() {
        super.onStart()

        conversationRecyclerView.onStart()
            .onError {
                Toast.makeText(this@ConversationsActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }.onNext {
                progress.visibility = View.GONE
            }
    }

    override fun onStop() {
        super.onStop()

        conversationRecyclerView.onStop()
    }
}


